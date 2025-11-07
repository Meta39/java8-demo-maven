package com.fu.springbootdynamicservicedemo.dynamicserviceregistry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/*
| 场景编号 | 测试内容                  | 核心断言点              |
| ---- | --------------------- | ------------------ |
| ①    | 单参数是集合（body 传数组）      | 能正确反序列化 List       |
| ②    | 单参数是集合（body 传标量）      | 自动包成 `[]` 再反序列化    |
| ⑤    | 多参数按顺序（body 是数组）      | 参数顺序映射正常           |
| ⑥    | 空 body                | 所有参数均为 null        |
| ⑦    | 参数校验失败                | `@NotNull` 参数触发异常  |
| ⑧    | 标量包集合（多参数其中一个是 List）  | 标量自动包装为集合          |
 */

/**
 * 修正了集合/数组目标类型接收标量值时报错的问题：
 * - 单参数场景：如果目标是集合/数组且 body 不是数组，包一层 [] 再反序列化
 * - 多参数按字段场景：如果字段是标量但目标是集合/数组，创建 ArrayNode 包装后再 convertValue
 * 同时包裹并增强了 JSON 错误上下文信息。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicInvoker {
    private static final String SQUARE_BRACKETS_LEFT = "[";
    private static final String SQUARE_BRACKETS_RIGHT = "]";
    // 缓存常用类型判断结果
    private static final Set<Class<?>> PRIMITIVE_OR_WRAPPERS;

    static {
        Set<Class<?>> tempSet = new HashSet<>();
        tempSet.add(Boolean.class);
        tempSet.add(Byte.class);
        tempSet.add(Character.class);
        tempSet.add(Short.class);
        tempSet.add(Integer.class);
        tempSet.add(Long.class);
        tempSet.add(Float.class);
        tempSet.add(Double.class);
        PRIMITIVE_OR_WRAPPERS = Collections.unmodifiableSet(tempSet);
    }

    private final ApplicationContext context;
    private final DynamicMethodRegistry registry;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public Object invoke(String serviceName, String methodName, String body) throws Throwable {
        //获取 meta
        DynamicMethodRegistry.MethodMeta meta = registry.getMethodMeta(serviceName, methodName);
        //参数转换
        Object[] args = resolveArgsByMethodMeta(serviceName, methodName, meta, body);
        // 参数校验
        validateArgs(args);
        // 执行方法
        return meta.isVoidReturn() ? null : meta.getHandle().invokeWithArguments(args);
    }

    //不能存在同名方法。
    public Object invokeNoCache(String beanName, String methodName, String body) throws Throwable {
        Object bean = context.getBean(beanName);
        Method method = checkAndGetMethod(beanName, methodName, AopUtils.getTargetClass(bean).getDeclaredMethods());
        DynamicMethodRegistry.checkPublicMethod(beanName, methodName, method.getModifiers());
        return checkAndReturn(method.getReturnType(), MethodHandles
                .lookup()
                .unreflect(method)
                .bindTo(bean)
                .invokeWithArguments(resolveArgsNoCache(beanName, methodName, method.getParameters(), body)));
    }

    private Method checkAndGetMethod(String beanName, String methodName, Method[] methods) {
        int count = 0;
        Method m = null;
        for (Method method : methods) {
            //如果同一个类出现同名方法，则抛出异常。
            if (count > 1) {
                DynamicMethodRegistry.cheackSameMethodInTheSameClass(beanName, methodName);
            }
            if (method.getName().equals(methodName)) {
                m = method;
                count++;
            }
        }
        if (m == null) {
            throw new IllegalArgumentException("not find " + beanName + "." + methodName);
        }
        return m;
    }

    private static Object checkAndReturn(Class<?> returnType, Object ret) {
        return isVoidReturn(returnType) ? null : ret;
    }

    public static boolean isVoidReturn(Class<?> returnType) {
        return returnType == void.class || returnType == Void.class;
    }

    //参数校验
    private void validateArgs(Object[] args) {
        if (args == null) return;

        for (Object arg : args) {
            if (arg == null || isPrimitiveOrWrapper(arg.getClass())) continue;

            Set<ConstraintViolation<Object>> violations = validator.validate(arg);
            if (!CollectionUtils.isEmpty(violations)) {
                ConstraintViolation<Object> violation = violations.iterator().next();
                //有一个参数校验失败就立即抛出异常，而不是全部校验完才抛出，这样可以提高性能并提供更及时的反馈。
                throw new IllegalArgumentException(violation.getPropertyPath() + " " + violation.getMessage());
            }
        }
    }

    private Object[] resolveArgsNoCache(String serviceName, String methodName, Parameter[] parameters, String body) {
        int paramCount = parameters.length;
        String[] paramNames = new String[paramCount];
        JavaType[] jacksonTypes = new JavaType[paramCount];
        // TypeFactory 缓存，避免在循环中多次获取
        final TypeFactory typeFactory = objectMapper.getTypeFactory();
        for (int i = 0; i < paramCount; i++) {
            Parameter param = parameters[i];
            paramNames[i] = param.getName();
            jacksonTypes[i] = typeFactory.constructType(param.getParameterizedType());
        }
        return resolveArgsFromJson(serviceName, methodName, paramCount, jacksonTypes, paramNames, body);
    }

    private Object[] resolveArgsByMethodMeta(String serviceName, String methodName, DynamicMethodRegistry.MethodMeta meta, String body) {
        return resolveArgsFromJson(serviceName, methodName, meta.getParamTypesLength(), meta.getJacksonParamTypes(), meta.getParamNames(), body);
    }

    private Object[] resolveArgsFromJson(String serviceName, String methodName, int paramLen, JavaType[] jacksonParamTypes, String[] paramNames, String body) {
        try {
            return resolveArgs(paramLen, jacksonParamTypes, paramNames, body);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(String.format("Failed to parse JSON parameter (%s.%s): %s", serviceName, methodName, e.getOriginalMessage()), e);
        }
    }

    private Object[] resolveArgs(int paramLen, JavaType[] jacksonParamTypes, String[] paramNames, String body) throws JsonProcessingException {
        if (paramLen == 0) return new Object[0];
        if (!StringUtils.hasText(body)) return new Object[paramLen];

        String trimmed = body.trim();

        // ---------- 如果是数组 JSON（例如 [..]） ----------
        if (trimmed.startsWith(SQUARE_BRACKETS_LEFT)) {
            //  单参数且参数类型是集合/数组时，直接映射整个数组
            JavaType oneJacksonParamType = jacksonParamTypes[0];
            if (paramLen == 1 && isJacksonCollectionLike(oneJacksonParamType)) {
                Object arg = objectMapper.readValue(body, oneJacksonParamType);
                return new Object[]{arg};
            }

            //  否则认为这是按参数顺序传的多个值
            Object[] raw = objectMapper.readValue(body, Object[].class);
            Object[] args = new Object[paramLen];
            for (int i = 0; i < paramLen && i < raw.length; i++) {
                Object value = raw[i];
                JavaType tempJacksonParamType = jacksonParamTypes[i];
                if ((value instanceof Number || value instanceof String)
                        && isJacksonCollectionLike(tempJacksonParamType)) {
                    // 把标量包成数组
                    String json = SQUARE_BRACKETS_LEFT + objectMapper.writeValueAsString(value) + SQUARE_BRACKETS_RIGHT;
                    args[i] = objectMapper.readValue(json, tempJacksonParamType);
                } else {
                    args[i] = objectMapper.convertValue(value, tempJacksonParamType);
                }
            }
            return args;
        }

        // ---------- 单参数场景 ----------
        if (paramLen == 1) {
            JavaType oneJacksonParamType = jacksonParamTypes[0];
            if (isJacksonCollectionLike(oneJacksonParamType)) {
                String wrapped = SQUARE_BRACKETS_LEFT + body + SQUARE_BRACKETS_RIGHT;
                Object arg = objectMapper.readValue(wrapped, oneJacksonParamType);
                return new Object[]{arg};
            } else {
                Object arg = objectMapper.readValue(body, oneJacksonParamType);
                return new Object[]{arg};
            }
        }

        // ---------- 多参数：按字段取值 ----------
        JsonNode root = objectMapper.readTree(body);
        Object[] args = new Object[paramLen];
        Map<String, JsonNode> fieldMap = new HashMap<>();
        // 直接使用 fields() 迭代更简洁
        Iterator<Map.Entry<String, JsonNode>> fit = root.fields();
        while (fit.hasNext()) {
            Map.Entry<String, JsonNode> e = fit.next();
            fieldMap.put(e.getKey(), e.getValue());
        }

        for (int i = 0; i < paramLen; i++) {
            String paramName = paramNames[i];
            JsonNode n = fieldMap.get(paramName);

            if (n == null) {
                args[i] = null;
            } else {
                JavaType tempJacksonParamType = jacksonParamTypes[i];
                if ((n.isValueNode() || n.isNumber() || n.isTextual()) && isJacksonCollectionLike(tempJacksonParamType)) {
                    ArrayNode arr = JsonNodeFactory.instance.arrayNode();
                    arr.add(n);
                    args[i] = objectMapper.convertValue(arr, tempJacksonParamType);
                } else {
                    args[i] = objectMapper.convertValue(n, tempJacksonParamType);
                }
            }
        }
        return args;
    }

    private boolean isJacksonCollectionLike(JavaType jt) {
        return jt != null && (jt.isCollectionLikeType() || jt.isArrayType());
    }

    //不能是 String，因为可能是 JSON 字符串
    private boolean isPrimitiveOrWrapper(Class<?> cls) {
        return cls.isPrimitive() || PRIMITIVE_OR_WRAPPERS.contains(cls);
    }

}

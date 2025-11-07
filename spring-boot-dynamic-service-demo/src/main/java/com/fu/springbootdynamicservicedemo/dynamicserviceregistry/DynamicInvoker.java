package com.fu.springbootdynamicservicedemo.dynamicserviceregistry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
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

/**
 * 动态方法调用器，支持：
 * 1. JSON 自动反序列化（含数组/集合兼容）
 * 2. 参数校验（JSR-303）
 * 3. 统一异常上下文信息
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicInvoker {

    private static final String SQUARE_BRACKETS_LEFT = "[";
    private static final String SQUARE_BRACKETS_RIGHT = "]";

    private static final Set<Class<?>> PRIMITIVE_OR_WRAPPERS;

    static {
        Set<Class<?>> temp = new HashSet<>(Arrays.asList(
                Boolean.class, Byte.class, Character.class, Short.class,
                Integer.class, Long.class, Float.class, Double.class
        ));
        PRIMITIVE_OR_WRAPPERS = Collections.unmodifiableSet(temp);
    }

    private final ApplicationContext context;
    private final DynamicMethodRegistry registry;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public Object invoke(String serviceName, String methodName, String body) throws Throwable {
        DynamicMethodRegistry.MethodMeta meta = registry.getMethodMeta(serviceName, methodName);
        Object[] args = resolveArgsByMeta(serviceName, methodName, meta, body);
        validateArgs(args);
        return meta.isVoidReturn() ? null : meta.getHandle().invokeWithArguments(args);
    }

    /**
     * 无法调用重名方法，因为无法提前知道参数的格式和个数。所以调用的类出现重名方法会报错。
     */
    public Object invokeNoCache(String beanName, String methodName, String body) throws Throwable {
        Object bean = context.getBean(beanName);
        Method method = findUniqueMethod(beanName, methodName, AopUtils.getTargetClass(bean).getDeclaredMethods());
        DynamicMethodRegistry.checkPublicMethod(beanName, methodName, method.getModifiers());
        return checkReturn(method.getReturnType(),
                MethodHandles.lookup()
                        .unreflect(method)
                        .bindTo(bean)
                        .invokeWithArguments(resolveArgsNoCache(beanName, methodName, method.getParameters(), body))
        );
    }

    private Method findUniqueMethod(String beanName, String methodName, Method[] methods) {
        Method found = null;
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                if (found != null) {
                    DynamicMethodRegistry.cheackSameMethodInTheSameClass(beanName, methodName);
                }
                found = m;
            }
        }
        if (found == null) {
            throw new IllegalArgumentException("Method not found: " + beanName + "." + methodName);
        }
        return found;
    }

    private static Object checkReturn(Class<?> returnType, Object ret) {
        return DynamicMethodRegistry.isVoid(returnType) ? null : ret;
    }

    private void validateArgs(Object[] args) {
        if (args == null) return;

        for (Object arg : args) {
            if (arg == null || isPrimitiveOrWrapper(arg.getClass())) continue;
            Set<ConstraintViolation<Object>> violations = validator.validate(arg);
            if (!CollectionUtils.isEmpty(violations)) {
                ConstraintViolation<Object> v = violations.iterator().next();
                throw new IllegalArgumentException(v.getPropertyPath() + " " + v.getMessage());
            }
        }
    }

    private Object[] resolveArgsNoCache(String serviceName, String methodName, Parameter[] params, String body) {
        TypeFactory tf = objectMapper.getTypeFactory();
        int len = params.length;
        String[] names = new String[len];
        JavaType[] types = new JavaType[len];
        for (int i = 0; i < len; i++) {
            names[i] = params[i].getName();
            types[i] = tf.constructType(params[i].getParameterizedType());
        }
        return resolveArgsFromJson(serviceName, methodName, len, types, names, body);
    }

    private Object[] resolveArgsByMeta(String serviceName, String methodName, DynamicMethodRegistry.MethodMeta meta, String body) {
        return resolveArgsFromJson(serviceName, methodName, meta.getParamTypesLength(), meta.getJacksonParamTypes(), meta.getParamNames(), body);
    }

    private Object[] resolveArgsFromJson(String serviceName, String methodName, int len, JavaType[] types, String[] names, String body) {
        try {
            return resolveArgs(len, types, names, body);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(String.format("Failed to parse JSON (%s.%s): %s",
                    serviceName, methodName, e.getOriginalMessage()), e);
        }
    }

    private Object[] resolveArgs(int len, JavaType[] types, String[] names, String body) throws JsonProcessingException {
        if (len == 0) return new Object[0];
        if (!StringUtils.hasText(body)) return new Object[len];

        String trimmed = body.trim();

        if (trimmed.startsWith(SQUARE_BRACKETS_LEFT)) {
            return resolveArrayArgs(len, types, body);
        } else if (len == 1) {
            return new Object[]{resolveSingleArg(types[0], body)};
        } else {
            return resolveObjectArgs(len, types, names, body);
        }
    }

    private Object[] resolveArrayArgs(int len, JavaType[] types, String body) throws JsonProcessingException {
        if (len == 1 && isCollectionLike(types[0])) {
            return new Object[]{objectMapper.readValue(body, types[0])};
        }
        Object[] raw = objectMapper.readValue(body, Object[].class);
        Object[] args = new Object[len];
        for (int i = 0; i < len && i < raw.length; i++) {
            args[i] = adaptCollectionValue(raw[i], types[i]);
        }
        return args;
    }

    private Object[] resolveObjectArgs(int len, JavaType[] types, String[] names, String body) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(body);
        Object[] args = new Object[len];
        for (int i = 0; i < len; i++) {
            JsonNode node = root.get(names[i]);
            args[i] = node == null ? null : adaptCollectionNode(node, types[i]);
        }
        return args;
    }

    private Object resolveSingleArg(JavaType type, String body) throws JsonProcessingException {
        return isCollectionLike(type)
                ? objectMapper.readValue(SQUARE_BRACKETS_LEFT + body + SQUARE_BRACKETS_RIGHT, type)
                : objectMapper.readValue(body, type);
    }

    private Object adaptCollectionValue(Object value, JavaType targetType) throws JsonProcessingException {
        if ((value instanceof Number || value instanceof String) && isCollectionLike(targetType)) {
            String json = SQUARE_BRACKETS_LEFT + objectMapper.writeValueAsString(value) + SQUARE_BRACKETS_RIGHT;
            return objectMapper.readValue(json, targetType);
        }
        return objectMapper.convertValue(value, targetType);
    }

    private Object adaptCollectionNode(JsonNode node, JavaType targetType) {
        if (node.isValueNode() && isCollectionLike(targetType)) {
            ArrayNode arr = JsonNodeFactory.instance.arrayNode().add(node);
            return objectMapper.convertValue(arr, targetType);
        }
        return objectMapper.convertValue(node, targetType);
    }

    private boolean isCollectionLike(JavaType type) {
        return type != null && (type.isCollectionLikeType() || type.isArrayType());
    }

    private boolean isPrimitiveOrWrapper(Class<?> cls) {
        return cls.isPrimitive() || PRIMITIVE_OR_WRAPPERS.contains(cls);
    }
}

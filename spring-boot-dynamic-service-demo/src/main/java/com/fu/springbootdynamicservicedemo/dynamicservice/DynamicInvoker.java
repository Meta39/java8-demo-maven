package com.fu.springbootdynamicservicedemo.dynamicservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 修正了集合/数组目标类型接收标量值时报错的问题：
 * - 单参数场景：如果目标是集合/数组且 body 不是数组，包一层 [] 再反序列化
 * - 多参数按字段场景：如果字段是标量但目标是集合/数组，创建 ArrayNode 包装后再 convertValue
 */
@Component
@RequiredArgsConstructor
public class DynamicInvoker {

    private final DynamicMethodRegistry registry;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public Object invoke(String serviceName, String methodName, String body) throws Throwable {
        DynamicMethodRegistry.MethodMeta meta = registry.getMethodMeta(serviceName, methodName);

        Object[] args = resolveArgs(meta, body);

        // 参数校验
        for (Object arg : args) {
            if (arg == null) continue;
            if (isPrimitiveOrWrapper(arg.getClass())) continue;
            Set<ConstraintViolation<Object>> violations = validator.validate(arg);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder("参数校验失败: ");
                for (ConstraintViolation<Object> v : violations) {
                    sb.append("[").append(v.getPropertyPath()).append(" ").append(v.getMessage()).append("]");
                }
                throw new IllegalArgumentException(sb.toString());
            }
        }
        MethodHandle metaHandle = meta.getHandle();
        Object ret = metaHandle.invokeWithArguments(args);
        if (meta.isVoidReturn()) return null;
        return ret;
    }

    private Object[] resolveArgs(DynamicMethodRegistry.MethodMeta meta, String body) throws JsonProcessingException {
        Class<?>[] paramTypes = meta.getParamTypes();
        int paramLen = paramTypes.length;
        if (paramLen == 0) return new Object[0];
        if (!StringUtils.hasText(body)) {
            return new Object[paramLen];
        }

        String trimmed = body.trim();

        // ---------- 如果是数组 JSON（例如 [..]） ----------
        if (trimmed.startsWith("[")) {
            //  单参数且参数类型是集合/数组时，直接映射整个数组
            JavaType oneJacksonParamType = meta.getJacksonParamTypes()[0];
            if (paramLen == 1 && isJacksonCollectionLike(oneJacksonParamType)) {
                Object arg = objectMapper.readValue(body, oneJacksonParamType);
                return new Object[]{arg};
            }

            //  否则认为这是按参数顺序传的多个值
            Object[] raw = objectMapper.readValue(body, Object[].class);
            Object[] args = new Object[paramLen];
            for (int i = 0; i < paramLen && i < raw.length; i++) {
                Object value = raw[i];
                JavaType tempJacksonParamType = meta.getJacksonParamTypes()[i];
                if ((value instanceof Number || value instanceof String)
                        && isJacksonCollectionLike(tempJacksonParamType)) {
                    // 把标量包成数组
                    String json = "[" + objectMapper.writeValueAsString(value) + "]";
                    args[i] = objectMapper.readValue(json, tempJacksonParamType);
                } else {
                    args[i] = objectMapper.convertValue(value, tempJacksonParamType);
                }
            }
            return args;
        }

        // ---------- 单参数场景 ----------
        if (paramLen == 1) {
            JavaType oneJacksonParamType = meta.getJacksonParamTypes()[0];
            if (isJacksonCollectionLike(oneJacksonParamType)) {
                String wrapped = "[" + body + "]";
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
        String[] paramNames = meta.getParamNames();
        Map<String, JsonNode> fieldMap = new HashMap<>();
        Iterator<String> it = root.fieldNames();
        while (it.hasNext()) {
            String fn = it.next();
            fieldMap.put(fn, root.get(fn));
        }

        for (int i = 0; i < paramLen; i++) {
            String paramName = paramNames[i];
            JsonNode n = fieldMap.get(paramName);
            if (n == null) {
                String snakeCase = toSnakeCase(paramName);
                n = fieldMap.get(snakeCase);
            }

            if (n == null) {
                args[i] = null;
            } else {
                JavaType tempJacksonParamType = meta.getJacksonParamTypes()[i];
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
        if (jt == null) return false;
        return jt.isCollectionLikeType() || jt.isArrayType();
    }

    private boolean isPrimitiveOrWrapper(Class<?> cls) {
        if (cls.isPrimitive()) return true;
        // 常见包装类型判断
        return cls == Boolean.class || cls == Byte.class || cls == Character.class ||
                cls == Short.class || cls == Integer.class || cls == Long.class ||
                cls == Float.class || cls == Double.class;
    }

    private String toSnakeCase(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append('_').append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}


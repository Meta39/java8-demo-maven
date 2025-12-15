package com.fu.springbootdynamicservicedemo.dynamicserviceregistry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
        PRIMITIVE_OR_WRAPPERS = Collections.unmodifiableSet(new HashSet<Class<?>>() {{
            this.add(Boolean.class);
            this.add(Character.class);
            this.add(Byte.class);
            this.add(Short.class);
            this.add(Integer.class);
            this.add(Long.class);
            this.add(Float.class);
            this.add(Double.class);
            this.add(Void.class);
        }});
    }

    private final DynamicMethodRegistry registry;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public Object invoke(String serviceName, String methodName, String body) throws Throwable {
        DynamicMethodRegistry.MethodHandleMeta meta = registry.getMethodMeta(serviceName, methodName);
        Object[] args = resolveArgsByMeta(serviceName, methodName, meta, body);
        validateArgs(args);
        return meta.isVoidReturn() ? null : meta.invokeWithArguments(args);
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

    private Object[] resolveArgsByMeta(String serviceName, String methodName, DynamicMethodRegistry.MethodHandleMeta meta, String body) {
        return resolveArgsFromJson(serviceName, methodName, meta.getParamTypesLength(), meta.getJacksonParamTypes(), meta.getParamNames(), body);
    }

    private Object[] resolveArgsFromJson(String serviceName, String methodName, int len, JavaType[] types, String[] names, String body) {
        try {
            return resolveArgs(len, types, names, body);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(String.format("Failed to parse JSON (%s.%s): %s", serviceName, methodName, e.getOriginalMessage()), e);
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
        return isCollectionLike(type) ? objectMapper.readValue(SQUARE_BRACKETS_LEFT + body + SQUARE_BRACKETS_RIGHT, type) : objectMapper.readValue(body, type);
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

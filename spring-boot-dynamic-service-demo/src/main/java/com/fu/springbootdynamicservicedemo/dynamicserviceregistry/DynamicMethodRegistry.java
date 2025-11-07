package com.fu.springbootdynamicservicedemo.dynamicserviceregistry;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fu.springbootdynamicservicedemo.annotation.DynamicMethod;
import com.fu.springbootdynamicservicedemo.annotation.DynamicService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自动扫描并注册所有 @DynamicService Bean 与其 @DynamicMethod。
 * 支持使用 beanName 作为默认 serviceName。
 * 兼容 Java 8。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicMethodRegistry {

    private final ApplicationContext context;
    private final ObjectMapper objectMapper;

    // serviceName -> (methodName -> MethodMeta)
    private final Map<String, Map<String, MethodMeta>> registry = new ConcurrentHashMap<>();

    //校验并获取
    public MethodMeta getMethodMeta(String serviceName, String methodName) {
        Map<String, MethodMeta> methods = registry.get(serviceName);
        if (methods == null) {
            throw new IllegalArgumentException("Service not found: " + serviceName);
        }

        MethodMeta meta = methods.get(methodName);
        if (meta == null) {
            throw new IllegalArgumentException("Method not found: " + serviceName + "." + methodName);
        }
        return meta;
    }

    /**
     * 自动初始化（Spring 容器启动后自动扫描）
     */
    @PostConstruct
    public void init() throws IllegalAccessException {
        Map<String, Object> beans = context.getBeansWithAnnotation(DynamicService.class);
        if (CollectionUtils.isEmpty(beans)) {
            log.warn("No @DynamicService Bean was found");
            return;
        }

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        // TypeFactory 缓存，避免在循环中多次获取
        final TypeFactory typeFactory = objectMapper.getTypeFactory();

        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            String beanName = entry.getKey();
            Object bean = entry.getValue();

            Class<?> targetClass = AopUtils.getTargetClass(bean);
            DynamicService ds = targetClass.getAnnotation(DynamicService.class);
            assert ds != null;
            String serviceName = StringUtils.hasText(ds.value()) ? ds.value() : beanName;

            Map<String, MethodMeta> methods = new HashMap<>();
            for (Method method : targetClass.getDeclaredMethods()) {
                DynamicMethod dm = method.getAnnotation(DynamicMethod.class);
                if (dm == null) continue;

                checkPublicMethod(beanName, method.getName(), method.getModifiers());

                String dmValue = dm.value();
                String methodName = StringUtils.hasText(dmValue) ? dmValue : method.getName();
                if (methods.containsKey(methodName)) {
                    cheackSameMethodInTheSameClass(serviceName, methodName);
                }

                MethodHandle handle = lookup.unreflect(method).bindTo(bean);
                //创建 MethodMeta
                MethodMeta meta = createMethodMeta(method, handle, typeFactory);
                methods.put(methodName, meta);
            }

            if (!methods.isEmpty()) {
                registry.put(serviceName, methods);
                log.info("Register @DynamicService bean:[{}] methods:{}", serviceName, methods.keySet());
            } else {
                log.warn("@DynamicService (bean:{}) does not have the @DynamicMethod annotation", serviceName);
            }
        }
    }

    public static void cheackSameMethodInTheSameClass(String beanName, String methodName) {
        throw new IllegalStateException("The same method name occurs in the same @DynamicService class: " + beanName + "." + methodName);
    }

    public static void checkPublicMethod(String beanName, String methodName, int modifiers) {
        if (!Modifier.isPublic(modifiers)) {
            throw new IllegalStateException("@DynamicMethod must be public: " + beanName + "." + methodName);
        }
    }

    private MethodMeta createMethodMeta(Method method, MethodHandle handle, TypeFactory typeFactory) {
        Parameter[] parameters = method.getParameters();
        int paramCount = parameters.length;

        String[] paramNames = new String[paramCount];
        JavaType[] jacksonTypes = new JavaType[paramCount];

        for (int i = 0; i < paramCount; i++) {
            Parameter param = parameters[i];
            paramNames[i] = param.getName();
            jacksonTypes[i] = typeFactory.constructType(param.getParameterizedType());
        }

        return new MethodMeta(handle, method.getReturnType() == void.class, paramCount, paramNames, jacksonTypes);
    }

    @Getter
    @AllArgsConstructor
    public static class MethodMeta {
        private final MethodHandle handle;
        private final boolean voidReturn;
        private final int paramTypesLength;
        private final String[] paramNames;
        private final JavaType[] jacksonParamTypes;
    }
}


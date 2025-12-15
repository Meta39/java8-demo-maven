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
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自动扫描并注册所有 @DynamicService Bean 与其 @DynamicMethod。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicMethodRegistry {

    private final ApplicationContext context;
    private final ObjectMapper objectMapper;

    //key: bean name + method name
    private final Map<String, MethodMeta> registry = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() throws IllegalAccessException {
        Map<String, Object> beans = context.getBeansWithAnnotation(DynamicService.class);
        if (CollectionUtils.isEmpty(beans)) {
            log.warn("No @DynamicService Bean found");
            return;
        }

        //因为初始化是单线程的，所以下面这种方式是安全的。
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        TypeFactory tf = objectMapper.getTypeFactory();

        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            registerService(entry.getKey(), entry.getValue(), lookup, tf);
        }
    }

    private void registerService(String beanName, Object bean, MethodHandles.Lookup lookup, TypeFactory tf) throws IllegalAccessException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        DynamicService ds = targetClass.getAnnotation(DynamicService.class);
        String serviceName = StringUtils.hasText(ds.value()) ? ds.value() : beanName;

        for (Method m : targetClass.getDeclaredMethods()) {
            DynamicMethod dm = m.getAnnotation(DynamicMethod.class);
            if (dm == null) continue;

            checkPublicMethod(serviceName, m.getName(), m.getModifiers());
            String methodName = StringUtils.hasText(dm.value()) ? dm.value() : m.getName();

            String cacheKey = buildKey(serviceName, methodName);
            if (registry.containsKey(cacheKey)) {
                cheackSameMethodInTheSameClass(serviceName, methodName);
            }

            registry.put(cacheKey, createMeta(m, lookup.unreflect(m).bindTo(bean), tf));
            log.info("Registered service [{}] with methods: {}", serviceName, registry.keySet());
        }
    }

    private String buildKey(String serviceName, String methodName) {
        return serviceName + "." + methodName;
    }

    public MethodMeta getMethodMeta(String service, String method) {
        MethodMeta meta = registry.get(buildKey(service, method));
        if (meta == null) {
            throw new IllegalArgumentException("Method not found: " + service + "." + method);
        }
        return meta;
    }

    public static void cheackSameMethodInTheSameClass(String beanName, String methodName) {
        throw new IllegalStateException("Duplicate method in same class: " + beanName + "." + methodName);
    }

    public static void checkPublicMethod(String beanName, String methodName, int modifiers) {
        if (!Modifier.isPublic(modifiers)) {
            throw new IllegalStateException("@DynamicMethod must be public: " + beanName + "." + methodName);
        }
    }

    private MethodMeta createMeta(Method m, MethodHandle handle, TypeFactory tf) {
        Parameter[] params = m.getParameters();
        int len = params.length;
        String[] names = new String[len];
        JavaType[] types = new JavaType[len];
        for (int i = 0; i < len; i++) {
            names[i] = params[i].getName();
            types[i] = tf.constructType(params[i].getParameterizedType());
        }
        return new MethodMeta(handle, isVoid(m.getReturnType()), len, names, types);
    }

    public static boolean isVoid(Class<?> returnType) {
        return returnType == void.class || returnType == Void.class;
    }

    @Getter
    @AllArgsConstructor
    public static class MethodMeta {
        private final MethodHandle handle;
        private final boolean voidReturn;
        private final int paramTypesLength;
        private final String[] paramNames;
        private final JavaType[] jacksonParamTypes;

        public Object invokeWithArguments(Object... arguments) throws Throwable {
            return handle.invokeWithArguments(arguments);
        }

    }
}

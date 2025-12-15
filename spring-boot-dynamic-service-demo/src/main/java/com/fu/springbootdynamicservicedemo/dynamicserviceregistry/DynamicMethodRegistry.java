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
    private final Map<String, MethodHandleMeta> registry = new ConcurrentHashMap<>();

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

    public MethodHandleMeta getMethodMeta(String service, String method) {
        MethodHandleMeta meta = registry.get(buildKey(service, method));
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

    private MethodHandleMeta createMeta(Method m, MethodHandle handle, TypeFactory tf) {
        Parameter[] params = m.getParameters();
        int len = params.length;
        String[] names = new String[len];
        JavaType[] types = new JavaType[len];
        for (int i = 0; i < len; i++) {
            names[i] = params[i].getName();
            types[i] = tf.constructType(params[i].getParameterizedType());
        }
        return new MethodHandleMeta(handle, isVoid(m.getReturnType()), len, names, types);
    }

    public static boolean isVoid(Class<?> returnType) {
        return returnType == void.class || returnType == Void.class;
    }

    /**
     * 高性能（接近源生调用，但是会有额外的操作，适用于频繁调用的场景）
     */
    @Getter
    @AllArgsConstructor
    public static class MethodHandleMeta {
        private final MethodHandle handle;
        private final boolean voidReturn;
        private final int paramTypesLength;
        private final String[] paramNames;
        private final JavaType[] jacksonParamTypes;

        public Object invokeWithArguments(Object... arguments) throws Throwable {
            return handle.invokeWithArguments(arguments);
        }

    }

    /**
     * 普通反射调用（依赖JVM优化）
     */
    @Getter
    public static class MethodMeta {
        private final Object bean;
        private final Method method;
        private final boolean voidReturn;
        private final int paramTypesLength;
        private final Class<?>[] paramTypes;
        private final Class<?> returnType;

        public MethodMeta(Object object, Method method) {
            this.bean = object;
            this.method = method;
            this.voidReturn = isVoid(method.getReturnType());
            this.paramTypesLength = method.getParameters().length;
            this.paramTypes = method.getParameterTypes();
            this.returnType = method.getReturnType();
        }

        public Object invoke(Object... args) throws Exception {
            try {
                return method.invoke(bean, args);
            } catch (InvocationTargetException e) {
                //捕获的异常必须转为Exception，防止异常被吞掉。比如：抛出的自定义异常信息为：报错了，但是invoke执行失败，抛出了异常为null，把"报错了"给顶掉了，最终返回的错误只有null。
                throw (Exception) e.getTargetException();
            }
        }

    }

}

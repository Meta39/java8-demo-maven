package com.fu.springbootdynamicservicedemo.dynamicservice;

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
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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

    public boolean hasService(String serviceName) {
        return registry.containsKey(serviceName);
    }

    public boolean hasMethod(String serviceName, String methodName) {
        boolean hasService = hasService(serviceName);
        Map<String, MethodMeta> dynamicService = getDynamicService(serviceName);
        return hasService && dynamicService.containsKey(methodName);
    }

    public MethodMeta getMethodMeta(String serviceName, String methodName) {
        Map<String, MethodMeta> methods = getDynamicService(serviceName);
        return methods.get(methodName);
    }

    public Map<String, MethodMeta> getDynamicService(String serviceName) {
        return registry.get(serviceName);
    }

    /**
     * 自动初始化（Spring 容器启动后自动扫描）
     */
    @PostConstruct
    public void init() throws IllegalAccessException {
        Map<String, Object> beans = context.getBeansWithAnnotation(DynamicService.class);
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        Set<Map.Entry<String, Object>> beanentrySet = beans.entrySet();
        for (Map.Entry<String, Object> entry : beanentrySet) {
            String beanName = entry.getKey();
            Object bean = entry.getValue();

            Class<?> targetClass = AopUtils.getTargetClass(bean);
            DynamicService ds = targetClass.getAnnotation(DynamicService.class);
            if (Objects.isNull(ds)) continue;
            String dsValue = ds.value();
            String serviceName = StringUtils.hasText(dsValue) ? dsValue : beanName;

            Map<String, MethodMeta> methods = new HashMap<>();
            Method[] declaredMethods = targetClass.getDeclaredMethods();
            for (Method method : declaredMethods) {
                DynamicMethod dm = method.getAnnotation(DynamicMethod.class);
                if (dm == null) continue;

                if (!Modifier.isPublic(method.getModifiers())) {
                    throw new IllegalStateException("@DynamicMethod 方法必须为 public: " + method);
                }

                String dmValue = dm.value();
                String methodName = StringUtils.hasText(dmValue) ? dmValue : method.getName();
                if (methods.containsKey(methodName)) {
                    throw new IllegalStateException("重复的 @DynamicMethod 名称: " + serviceName + "." + methodName);
                }

                MethodHandle handle = lookup.unreflect(method).bindTo(bean);

                Parameter[] parameters = method.getParameters();
                int parametersLength = parameters.length;
                Class<?>[] paramTypes = new Class[parametersLength];
                String[] paramNames = new String[parametersLength];
                JavaType[] jacksonTypes = new JavaType[parametersLength];

                for (int i = 0; i < parametersLength; i++) {
                    paramTypes[i] = parameters[i].getType();
                    paramNames[i] = parameters[i].getName();
                    TypeFactory typeFactory = objectMapper.getTypeFactory();
                    Type parameterizedType = parameters[i].getParameterizedType();
                    jacksonTypes[i] = typeFactory.constructType(parameterizedType);
                }

                MethodMeta meta = new MethodMeta(bean, method, handle, paramTypes, paramNames, jacksonTypes);
                methods.put(methodName, meta);
            }

            if (!methods.isEmpty()) {
                registry.put(serviceName, methods);
                log.info("注册 DynamicService [{}] 共 {} 个方法", serviceName, methods.size());
            }
        }
    }

    @Getter
    @AllArgsConstructor
    public static class MethodMeta {
        private final Object bean;
        private final Method method;
        private final MethodHandle handle;
        private final Class<?>[] paramTypes;
        private final String[] paramNames;
        private final JavaType[] jacksonParamTypes;

        public boolean isVoidReturn() {
            return method.getReturnType() == void.class;
        }
    }
}


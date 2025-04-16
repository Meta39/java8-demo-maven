package com.fu.springbootwebservicedemo.ws;

import com.fu.springbootwebservicedemo.util.ApplicationContextUtils;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 启动的时候把 IWebService实现类 handle函数的泛型入参写入缓存，在请求的时候直接通过缓存获取泛型入参的类型，
 * 减少每次请求的时候都使用反射获取泛型入参，提升程序性能。
 *
 * @since 2024-08-09
 */
@Component
public class WebServiceTypeCache implements ApplicationRunner {
    /**
     * 只能在启动的时候 put，运行的时候 get。不能在运行的时候 put，因为 HashMap 不是线程安全的。
     */
    private static final Map<String, Class<?>> typeCache = new HashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, IWebService> beans = ApplicationContextUtils.getBeansOfType(IWebService.class);
        //循环map，forEach(key,value) 是最现代的方式，使用起来简洁明了。也可以用 for (Map.Entry<String, IWebService> entry : beans.entrySet()){}。
        beans.forEach((bean, type) -> {
            // AopProxyUtils.ultimateTargetClass 解决Spring Boot 使用 @Transactional 事务注解的问题。
            Class<?> beanClass = AopProxyUtils.ultimateTargetClass(type);
            // 获取 IWebService 实现类的泛型类型
            Type[] genericInterfaces = beanClass.getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                if (genericInterface instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

                    if (actualTypeArguments.length > 0) {
                        Class<?> parameterType = (Class<?>) actualTypeArguments[0];
                        //把泛型入参放入缓存。防止每次请求都通过反射获取入参，影响程序性能。
                        typeCache.put(bean, parameterType);
                    }
                }
            }
        });
    }

    /**
     * 通过缓存获取 IWebService 实现类 handle 函数的 泛型入参
     *
     * @param serviceName IWebService实现类的 bean name
     */
    public static Class<?> getParameterType(String serviceName) {
        return typeCache.get(serviceName);
    }

}

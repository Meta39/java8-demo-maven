package com.fu.springbootwebservicedemo.ws;

import com.fu.springbootwebservicedemo.util.BeanUtils;
import org.springframework.aop.support.AopUtils;
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

    /**
     * 通过缓存获取 IWebService 实现类 handle 函数的 泛型入参
     *
     * @param serviceName IWebService实现类的 bean name
     */
    public static Class<?> getParameterType(String serviceName) {
        return typeCache.get(serviceName);
    }

    @Override
    public void run(ApplicationArguments args) {
        //循环map，forEach(key,value) 是最现代的方式，使用起来简洁明了。也可以用 for (Map.Entry<String, IWebService> entry : beans.entrySet()){}。
        BeanUtils
                .getBeansOfType(IWebService.class)
                .forEach((bean, type) -> {
            // AopUtils.getTargetClass 解决Spring Boot 使用 @Transactional 事务注解的问题。
            // 获取 IWebService 实现类的泛型类型
            for (Type genericInterface : AopUtils.getTargetClass(type).getGenericInterfaces()) {
                //ParameterizedType 是 Java 反射机制中的重要接口，表示参数化类型（泛型类型）。我们定义的IWebService接口是泛型接口，因此不是 ParameterizedType，则跳过。
                if (!(genericInterface instanceof ParameterizedType)) continue;
                ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                //getActualTypeArguments 获取实际类型参数，即实现IWebService接口的泛型类型的具体类型。
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                //正常情况下这里不会是0，但是为了健壮性，还是保留。
                if (actualTypeArguments.length == 0) continue;
                //因为IWebService有且仅有一个参数（并且是泛型参数），所以我们获取第一个即可。
                Class<?> parameterType = (Class<?>) actualTypeArguments[0];
                //把泛型入参放入缓存。防止每次请求都通过反射获取入参，影响程序性能。
                typeCache.put(bean, parameterType);
            }
        });
    }

}

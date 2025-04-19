package com.fu.springbootdynamicdatasource.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BeanUtils implements ApplicationContextAware {
    private BeanUtils() {
    }

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    //通过name获取 Bean.（推荐，因为bean的name是唯一的，出现重名的bean启动会报错。）
    public static Object getBean(String name) {
        return context.getBean(name);
    }

    //通过class获取Bean.（确保bean的name不会重复。因为可能会出现在不同包的同名bean导致获取到2个实例）
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean（这个是最稳妥的）
    public static <T> T getBean(String name, Class<T> clazz) {
        return context.getBean(name, clazz);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return context.getBeansOfType(clazz);
    }

}

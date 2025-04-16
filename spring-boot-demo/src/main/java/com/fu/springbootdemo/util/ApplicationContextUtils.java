package com.fu.springbootdemo.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取Bean
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {
    //构造函数私有化，防止其它人实例化该对象
    private ApplicationContextUtils(){}
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.applicationContext = applicationContext;
    }

    //通过name获取 Bean.（推荐，因为bean的name是唯一的，出现重名的bean启动会报错。）
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    //通过class获取Bean.（确保bean的name不会重复。因为可能会出现在不同包的同名bean导致获取到2个实例）
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean（这个是最稳妥的）
    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

}

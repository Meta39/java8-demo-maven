package com.fu.springbootwebservicedemo.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * jackson 工具类
 */
public abstract class JacksonUtils {
    public static final ObjectMapper JSON;
    public static final ObjectMapper XML;

    /*
        静态块
        作用：静态块用于初始化类的静态成员变量或执行一些只需要执行一次的静态操作。它在类加载时执行，且只执行一次。
        执行时机：类加载时执行，早于任何对象的创建。因此在里面new对象是最合适的。
     */
    static {
        // json 配置
        JSON = new ObjectMapper();
        JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JSON.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JSON.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JSON.registerModule(new JavaTimeModule());//处理java8新日期时间类型
        // xml 配置
        XML = new XmlMapper();
        XML.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        XML.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        XML.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        XML.registerModule(new JavaTimeModule());//处理java8新日期时间类型
    }

}
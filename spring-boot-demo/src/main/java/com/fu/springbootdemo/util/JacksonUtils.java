package com.fu.springbootdemo.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * jackson 工具类
 */
public abstract class JacksonUtils {
    public static final JsonMapper JSON;
    public static final XmlMapper XML;

    static {
        // json 配置
        JSON = new JsonMapper();
        JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JSON.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JSON.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JSON.registerModule(new JavaTimeModule());
        // xml 配置
        XML = new XmlMapper();
        XML.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        XML.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        XML.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        XML.registerModule(new JavaTimeModule());
    }

    public static String writeValueAsStringJson(Object object) {
        try {
            return JSON.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> T readValueJson(String content, TypeReference<T> valueTypeRef) {
        try {
            return JSON.readValue(content, valueTypeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}

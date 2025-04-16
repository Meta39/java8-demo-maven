package com.fu.springbootwebservicedemo.ws;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返回类
 * 创建日期：2024-07-01
 */
@Data
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@JacksonXmlRootElement(localName = "R")
public class R<T> {
    @JacksonXmlProperty(localName = "Code")
    private Integer code;
    @JacksonXmlProperty(localName = "Message")
    private String message;
    @JacksonXmlProperty(localName = "Data")
    private T data;

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(200, "success", data);
    }

    public static <T> R<T> err(String message) {
        return err(400, message);
    }

    public static <T> R<T> err(Integer code, String message) {
        return new R<>(code, message, null);
    }

}
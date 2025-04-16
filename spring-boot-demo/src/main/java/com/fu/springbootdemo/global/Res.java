package com.fu.springbootdemo.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一返回类
 */
@Data
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)//私有无参构造
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)//私有全参构造
public class Res<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer code; //状态码
    private String msg; //返回消息
    private T data; //数据

    /**
     * 成功（前端没有特别要求状态码，无参数返回，直接用这个。）
     *
     */
    public static <T> Res<T> ok() {
        return ok("success", null);
    }

    /**
     * 成功（前端没有特别要求状态码，直接用这个。）
     *
     * @param data 数据
     */
    public static <T> Res<T> ok(T data) {
        return ok("success", data);
    }

    /**
     * 成功（注意：前端要求返回信息要具体的才用这个，否则不使用！）
     *
     * @param message 成功信息（注意：前端要求返回信息要具体的才用这个，否则不使用！）
     * @param data    数据
     */
    public static <T> Res<T> ok(String message, T data) {
        return new Res<>(1, message, data);
    }

    /**
     * 一般异常（注意：前端没有特别要求状态码，直接用这个。）
     *
     * @param msg 异常信息
     */
    public static <T> Res<T> err(String msg) {
        return err(0, msg);
    }

    /**
     * 全局捕获异常（注意：前端要求状态码为其它的才使用这个，否则不使用。）
     *
     * @param code         状态码（注意：前端无特殊要求不要使用这个！就算要使用也要在Code枚举类里枚举出来，方便到时候统一修改！）
     * @param msg 错误信息
     */
    public static <T> Res<T> err(Integer code, String msg) {
        return new Res<>(code, msg, null);
    }

}
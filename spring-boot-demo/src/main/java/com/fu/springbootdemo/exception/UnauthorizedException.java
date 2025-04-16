package com.fu.springbootdemo.exception;

/**
 * 未认证异常
 */
public class UnauthorizedException extends CommonException {
    private static final int code = 2;
    private static final String message = "Unauthorized 抱歉，您未认证身份，请先登录认证身份。";

    public UnauthorizedException() {
        super(code,message);
    }

    public UnauthorizedException(String message) {
        super(code,message);
    }

}
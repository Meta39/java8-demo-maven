package com.fu.springbootdemo.exception;

/**
 * 未授权异常
 */
public class ForbiddenException extends CommonException {
    private static final int code = 3;
    private static final String message = "Forbidden 抱歉，您当前没有访问权限，请联系管理员授权。";

    public ForbiddenException() {
        super(code,message);
    }

    public ForbiddenException(String message) {
        super(code,message);
    }

}
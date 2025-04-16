package com.fu.springbootsecuritydemo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(MyAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{
        String errMsg;
        if (authException instanceof AccountExpiredException) {
            errMsg = "账号过期";
        } else if (authException instanceof InternalAuthenticationServiceException) {
            errMsg = "用户不存在";
        } else if (authException instanceof BadCredentialsException) {
            errMsg = "用户名或密码错误";
        } else if (authException instanceof CredentialsExpiredException) {
            errMsg = "密码过期";
        } else if (authException instanceof DisabledException) {
            errMsg = "账号不可用";
        } else if (authException instanceof LockedException) {
            errMsg = "账号锁定";
        } else if (authException instanceof InsufficientAuthenticationException) {
            errMsg = "访问的资源需要认证";
        } else {
            errMsg = "其他错误";
        }

        log.error("AuthenticationException：",authException);
        ResponseException.returnException(response, errMsg);//错误信息返回给前端
    }
}


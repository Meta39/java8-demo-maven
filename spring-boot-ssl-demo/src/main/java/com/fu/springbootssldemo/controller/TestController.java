package com.fu.springbootssldemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestController {
    
    @GetMapping("/test")
    public String test(HttpServletRequest request) {
        String protocol = request.getScheme();
        int port = request.getServerPort();
        return String.format("当前使用协议: %s, 端口: %d", protocol, port);
    }

}
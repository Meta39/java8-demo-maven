package com.fu.springbootssldemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @GetMapping("/test")
    public String test() {
        return "HTTPS连接成功！当前时间：" + System.currentTimeMillis();
    }

}
package com.fu.springbootssldemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {
    private static final String HTTP_HOST = "http://localhost:99";
    private static final String HTTPS_HOST = "https://localhost";
    private final RestTemplate restTemplate;//默认配置
    private final RestTemplate skipSslRestTemplate;//跳过SSL验证配置
    
    @GetMapping("/test")
    public String test(HttpServletRequest request) {
        String protocol = request.getScheme();
        int port = request.getServerPort();
        return String.format("当前使用协议: %s, 端口: %d", protocol, port);
    }

    @GetMapping("/testNoSkip")
    public String testNoSkip() {
        return restTemplate.getForObject(HTTP_HOST + "/test", String.class);
    }

    @GetMapping("/testNoSkipSsl")
    public String testNoSkipSsl() {
        return restTemplate.getForObject(HTTPS_HOST + "/test", String.class);
    }

    @GetMapping("/testSkip")
    public String testSkip() {
        return skipSslRestTemplate.getForObject(HTTP_HOST + "/test", String.class);
    }

    @GetMapping("/testSkipSsl")
    public String testSkipSsl() {
        return skipSslRestTemplate.getForObject(HTTPS_HOST + "/test", String.class);
    }

}
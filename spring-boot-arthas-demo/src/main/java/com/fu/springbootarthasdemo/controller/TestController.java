package com.fu.springbootarthasdemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    /*
        解决方法重载参数个数问题 params.length==0
        trace com.fu.springbootarthasdemo.controller.TestController hello params.length==0
     */
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    /*
        解决方法重载参数个数问题 params.length==1
        trace com.fu.springbootarthasdemo.controller.TestController hello params.length==1
     */
    @GetMapping("/hello2")
    public String hello2(@RequestParam(name = "name") String name) {
        return "hello, " + name;
    }

    /*
        解决方法重载参数类型问题 'params[0] instanceof Integer'
        trace com.fu.springbootarthasdemo.controller.TestController hello 'params[0] instanceof Integer'
     */
    @GetMapping("/hello3")
    public String hello3(@RequestParam(name = "num") Integer num) {
        return "number: " + num;
    }

}

package com.fu.springbootsecuritydemo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @RequestMapping("hello")
    //对于hasRole这个方法来讲，ROLE_ 加不加都可以，它的方法会自动判断的。hasPermission需要自定义PermissionEvaluator才能使用。
    @PreAuthorize("hasRole('admin') || hasPermission('','user:hello')")
    public String test() {
        return "Hello Login Success!";
    }

}
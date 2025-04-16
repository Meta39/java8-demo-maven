package com.fu.springbootsecuritydemo.controller;

import com.fu.springbootsecuritydemo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 登录
     * @param username 用户名 默认：admin
     * @param password 密码 默认：admin  加密密码为：$2a$10$voVByDp713heOXHDigIUbu2mOIreU0nnwbKmokY5wYRvYRogaxGMG
     */
    @PostMapping("/login")
    public String login(@RequestParam("username")String username, @RequestParam("password")String password){
        return loginService.login(username,password);
    }

    /**
     * 不直到为什么不能用/logout为登出地址！！！
     */
    @GetMapping("/doLogout")
    public boolean logout(HttpServletRequest request){
        loginService.logout(request);
        return true;
    }

    @GetMapping("test")
    public boolean test(){
        return true;
    }

}



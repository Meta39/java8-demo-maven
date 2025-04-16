package com.fu.springbootsecuritydemo.service;


import javax.servlet.http.HttpServletRequest;

public interface LoginService {
    String login(String username,String password);
    void logout(HttpServletRequest request);
}


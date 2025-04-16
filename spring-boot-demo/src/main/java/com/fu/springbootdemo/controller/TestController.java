package com.fu.springbootdemo.controller;

import com.fu.springbootdemo.annotation.PreAuthorize;
import com.fu.springbootdemo.annotation.ReturnMeta;
import com.fu.springbootdemo.async.MainThread;
import com.fu.springbootdemo.util.CurrentLoginUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试类相关接口
 */
@RestController
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController {
    private final MainThread mainThread;

    @ReturnMeta //返回原始数据，不用全局返回类封装。
    @GetMapping("hello")
    public Integer hello(){
        return CurrentLoginUserUtil.getUserId();
    }

    /**
     * async异步线程
     */
    @GetMapping("async")
    public String async(){
        return this.mainThread.mainThread();
    }

    /**
     * 权限注解（超级管理员角色或配置'**'过滤地址跳过该鉴权注解）
     */
    @PreAuthorize("authorize")
    @GetMapping("authorize")
    public String authorize(){
        return "超级管理员角色或配置'**'过滤地址跳过该鉴权注解";
    }
}

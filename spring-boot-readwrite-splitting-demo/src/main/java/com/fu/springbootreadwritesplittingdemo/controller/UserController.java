package com.fu.springbootreadwritesplittingdemo.controller;

import com.fu.springbootreadwritesplittingdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建日期：2024-05-28
 */
@Slf4j
@RequestMapping("user")
@RestController
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;

    @GetMapping
    public void getUsers() {
        for (int i = 0; i < 10; i++) {
            log.info("第{}次：{}", i, userService.selectById(1L));
        }
    }

    @GetMapping("update")
    public void updateUser() {
        for (int i = 0; i < 10; i++) {
            userService.updateById();
        }
    }

}

package com.fu.springbootfeignseata1.controller;

import com.fu.springbootfeignseata1.entity.User;
import com.fu.springbootfeignseata1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建日期：2024-05-13
 */
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("user")
    public String user(@RequestParam(name = "rollback") boolean rollback) {
        User user = new User();
        user.setName("Meta");
        try {
            userService.insert(rollback, user);
        } catch (Exception e) {
            return "rollback";
        }
        return "success";
    }

}

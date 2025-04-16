package com.fu.springbootfeignseata2.controller;

import com.fu.springbootfeignseata2.entity.User;
import com.fu.springbootfeignseata2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建日期：2024-05-13
 */
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("user")
    public void user() {
        User user = new User();
        user.setName("Meta2");
        userService.insert(user);
    }

}

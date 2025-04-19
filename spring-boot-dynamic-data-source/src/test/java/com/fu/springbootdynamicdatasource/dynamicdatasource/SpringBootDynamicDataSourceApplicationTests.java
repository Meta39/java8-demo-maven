package com.fu.springbootdynamicdatasource.dynamicdatasource;

import com.fu.springbootdynamicdatasource.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringBootDynamicDataSourceApplicationTests {
    private final UserService userService;

    @Autowired
    public SpringBootDynamicDataSourceApplicationTests(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void contextLoads() {
        userService.select();
    }

    @Test
    public void test2() {
        userService.insertUser();
    }

}

package com.fu.springbootjtamybatisplusdemo;

import com.fu.springbootjtamybatisplusdemo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringBootJtaMybatisPlusDemoApplicationTest {
    private final UserService userService;

    @Autowired
    public SpringBootJtaMybatisPlusDemoApplicationTest(UserService userService) {
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

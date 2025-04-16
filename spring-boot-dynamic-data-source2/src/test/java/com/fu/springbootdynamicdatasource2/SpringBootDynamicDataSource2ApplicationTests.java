package com.fu.springbootdynamicdatasource2;

import com.fu.springbootdynamicdatasource2.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class SpringBootDynamicDataSource2ApplicationTests {
    @Autowired
    private UserService userService;

    @Test
    void test() {
        userService.getUsers();
    }

    @Test
    void test2() {
        userService.insertUser();
    }

}
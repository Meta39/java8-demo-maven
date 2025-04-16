package com.fu.mybatisplusdemo;

import com.fu.mybatisplusdemo.entity.User;
import com.fu.mybatisplusdemo.enums.SexEnum;
import com.fu.mybatisplusdemo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class UserMapperTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test(){
        User user = new User();
        user.setId(1);
        user.setName("admin");
        user.setNickName("超级管理员");
        user.setSex(SexEnum.WOMEN);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDeleted(0);
        System.out.println(this.userMapper.insert(user));
    }
}

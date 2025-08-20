package com.fu.mybatisplusdemo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fu.mybatisplusdemo.entity.User;
import com.fu.mybatisplusdemo.enums.SexEnum;
import com.fu.mybatisplusdemo.mapper.UserMapper;
import com.fu.mybatisplusdemo.util.MyBatisPlusUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootTest
public class UserMapperTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() {
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

    @Test
    public void myBatisPlusUtilsFindByPageTest() {
        List<User> allUsers = new ArrayList<>();
        MyBatisPlusUtils.findByPage(userMapper,
                new LambdaQueryWrapper<User>().like(User::getName, "M"),
                users -> {
                    users.forEach(user -> {log.info(user.getName());});
                    allUsers.addAll(users);
                }
        );
        log.info("allUsers: {},distinct Users:{}", allUsers.size(), allUsers.stream().map(User::getName).distinct().count());
    }

}

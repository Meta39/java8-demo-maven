package com.fu.springbootdynamicdatasource2.service;

import com.fu.springbootdynamicdatasource2.annotations.DynamicTransactional;
import com.fu.springbootdynamicdatasource2.entity.User;
import com.fu.springbootdynamicdatasource2.mapper.mysql1.MySQL1UserMapper;
import com.fu.springbootdynamicdatasource2.mapper.mysql2.MySQL2UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 创建日期：2024-05-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final MySQL1UserMapper mySQL1UserMapper;
    private final MySQL2UserMapper mySQL2UserMapper;

    @DynamicTransactional
    public void getUsers(){
        log.info("数据源1用户数据：{}", mySQL1UserMapper.selectById(1));//数据源1
        log.info("数据源2用户数据{}", mySQL2UserMapper.selectById(1));//数据源2
    }

    @DynamicTransactional
    public void insertUser() {
        mySQL2UserMapper.insert(new User(5,"哈哈"));
        mySQL1UserMapper.insert(new User(4,"哈哈"));
//        int i = 1 / 0;
    }

}

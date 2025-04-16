package com.fu.springbootdynamicdatasource.service;

import com.fu.springbootdynamicdatasource.entity.User;
import com.fu.springbootdynamicdatasource.mapper.mysql1.MySQL1UserMapper;
import com.fu.springbootdynamicdatasource.mapper.mysql2.MySQL2UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建日期：2024-05-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final MySQL1UserMapper mySQL1UserMapper;
    private final MySQL2UserMapper mySQL2UserMapper;

    public void select(){
        User user = mySQL1UserMapper.selectById(1);
        User user2 = mySQL2UserMapper.selectById(1);
        log.info("user:{}",user);
        log.info("user2:{}",user2);
    }

    @Transactional
    public void insertUser() {
        mySQL2UserMapper.insert(new User(2,"哈哈"));
        mySQL1UserMapper.insert(new User(2,"哈哈"));
        int i = 1 / 0;
    }

}

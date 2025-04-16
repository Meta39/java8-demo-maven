package com.fu.springbootreadwritesplittingdemo.service;

import com.fu.springbootreadwritesplittingdemo.config.ReadOnly;
import com.fu.springbootreadwritesplittingdemo.entity.User;
import com.fu.springbootreadwritesplittingdemo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创建日期：2024-05-28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public void insert(User user) {
        userMapper.insert(user);
    }

    public void updateById(User user) {
        userMapper.updateById(user);
    }

    public void deleteById(Long userId) {
        userMapper.deleteById(userId);
    }

    @ReadOnly
    public User selectById(Long userId) {
        return userMapper.selectById(userId);
    }

    public List<User> selectList() {
        return userMapper.selectList(null);
    }

    /**
     * 验证事务
     * 1、什么注解都不加，使用主数据源
     * 2、只使用 @Transactional 注解，不会切换数据源，只能修改主库数据。
     * 3、只使用 @ReadOnly 注解，会切换数据源，主/从库都会修改。
     * 4、同时使用 @Transactional + @ReadOnly 注解，会切换数据源，单只能修改主库数据。
     * 结论：@Transactional 和 @ReadOnly 只能2选1
     */
//    @Transactional
//    @ReadOnly//加了 @Transactional 事务注解，即使加了 @ReadOnly 动态数据源注解也不会写到从库。如果不加事务注解，使用 @ReadOnly 动态数据源，则会写入从库。
    public void updateById() {
        User user = selectById(1L);
        user.setName("哈哈" + Math.random());
        log.info("{}", user);
        updateById(user);
    }

}

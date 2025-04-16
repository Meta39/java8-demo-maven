package com.fu.springbootfeignseata2.service;

import com.fu.springbootfeignseata2.entity.User;
import com.fu.springbootfeignseata2.mapper.UserMapper;
import io.seata.core.context.RootContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建日期：2024-05-13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    /**
     * 调用方用 openfeign 必须引入spring-cloud-starter-alibaba-seata，否则被调用方获取到的xid可能会为null
     * PS：被调用方可以不用引入
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(User user) {
        log.info("seata2 xid = {}", RootContext.getXID());
        userMapper.insert(user);
    }

}

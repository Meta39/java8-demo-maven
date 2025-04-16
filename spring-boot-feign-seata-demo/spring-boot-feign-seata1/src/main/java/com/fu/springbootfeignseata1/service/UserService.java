package com.fu.springbootfeignseata1.service;

import com.fu.springbootfeignseata1.entity.User;
import com.fu.springbootfeignseata1.feign.UserFeignClient;
import com.fu.springbootfeignseata1.mapper.UserMapper;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 创建日期：2024-05-13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserFeignClient userFeignClient;

    /**
     * 全局事务
     * 1、pom.xml引入seata-spring-boot-starter
     * 2、pom.xml引入spring-cloud-starter-alibaba-seata（排除：seata-spring-boot-starter，因为前面已经引入了。）
     * 3、使用 @GlobalTransactional 全局事务注解开启全局事务控制（默认：Seata AT 模式）
     * 4、被调用方业务层需要引入 @Transactional 本地事务注解
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    public void insert(boolean rollback, User user) {
        log.info("seata1 xid = {}", RootContext.getXID());
        userMapper.insert(user);
        userFeignClient.user();
        if (rollback) {
            throw new RuntimeException("rollback");
        }
    }
}

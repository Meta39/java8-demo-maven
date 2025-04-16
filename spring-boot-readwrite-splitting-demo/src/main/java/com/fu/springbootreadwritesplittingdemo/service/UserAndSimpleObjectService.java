package com.fu.springbootreadwritesplittingdemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建日期：2024-05-29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAndSimpleObjectService {
    private final UserService userService;
    private final SimpleObjectService simpleObjectService;

    /**
     * 只读，不加事务，数据源切换成功
     */
    public void readOnly() {
        log.info("user: {}", userService.selectById(1L));
        log.info("simpleObject: {}", simpleObjectService.selectById(1L));
    }

    /**
     * 加了事务，数据源切换失败。读取的是主库的数据，只是切面还是会输出切换了数据源，其实本质上走的是主库。
     */
    @Transactional(rollbackFor = Exception.class)
    public void readWrite() {
        userService.updateById();
        log.info("simpleObject: {}", simpleObjectService.selectById(1L));
    }

}

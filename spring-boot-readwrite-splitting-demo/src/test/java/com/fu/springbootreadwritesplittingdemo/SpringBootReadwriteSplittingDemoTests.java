package com.fu.springbootreadwritesplittingdemo;

import com.fu.springbootreadwritesplittingdemo.service.SimpleObjectService;
import com.fu.springbootreadwritesplittingdemo.service.UserAndSimpleObjectService;
import com.fu.springbootreadwritesplittingdemo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 创建日期：2024-05-28
 */
@SpringBootTest
public class SpringBootReadwriteSplittingDemoTests {
    @Autowired
    private UserService userService;
    @Autowired
    private SimpleObjectService simpleObjectService;
    @Autowired
    private UserAndSimpleObjectService userAndSimpleObjectService;

    @Test
    public void contextLoads() {
        System.out.println(userService.selectById(1L));
        System.out.println(simpleObjectService.selectById(1L));
    }

    /**
     * 只读，不加事务，数据源切换成功
     */
    @Test
    public void readOnly() {
        userAndSimpleObjectService.readOnly();
    }

    /**
     * 加了事务，数据源切换失败。读取的是主库的数据，只是切面还是会输出切换了数据源，其实本质上走的是主库。
     */
    @Test
    public void readWrite() {
        userAndSimpleObjectService.readWrite();
    }

}

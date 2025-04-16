package com.fu.basedemo;

import com.fu.basedemo.designpattern.singleton.usecase.ConfigurationManager;
import com.fu.basedemo.designpattern.singleton.usecase.DatabaseAccess;
import com.fu.basedemo.designpattern.singleton.usecase.LoggerAsync;
import com.fu.basedemo.designpattern.singleton.usecase.SingletonCache;
import com.fu.basedemo.designpattern.singleton.usecase.async.AsyncExecutor;
import com.fu.basedemo.designpattern.singleton.usecase.async.AsyncInvocationHandler;
import com.fu.basedemo.designpattern.singleton.usecase.async.MyService;
import com.fu.basedemo.designpattern.singleton.usecase.async.MyServiceImpl;
import com.fu.basedemo.entity.User;
import org.junit.jupiter.api.Test;

/**
 * 单例模式使用场景测试
 *
 * @since 2024-07-25
 */
public class SingletonUseCaseTests {

    /**
     * 测试数据库连接池
     */
    @Test
    void testSingletonUseCase() {
        DatabaseAccess databaseAccess = new DatabaseAccess();
        System.out.println(databaseAccess.getUsers(User.class));;
    }

    /**
     * 测试配置管理
     */
    @Test
    void testConfigurationManager() {
        String app = ConfigurationManager.getProperty("params.app");
        System.out.println(app);
    }

    /**
     * 测试线程池管理
     */
    public static void main(String[] args) {

        // 创建 MyService 的代理实例
        MyService myService = AsyncInvocationHandler.createAsyncProxy(new MyServiceImpl(), MyService.class);

        // 调用异步方法
        for (int i = 0; i < 10000; i++) {
            myService.asyncMethod();
        }

        // 调用普通方法
        myService.normalMethod();

        // 关闭线程池
        AsyncExecutor.shutdown();
    }

    /**
     * 测试缓存
     */
    @Test
    void testSingletonCache() {
        SingletonCache.put("key", "value");
        System.out.println(SingletonCache.get("key"));
    }

    /**
     * 测试日志记录
     */
    @Test
    void testLogger() {
        LoggerAsync logger = LoggerAsync.getInstance();
        logger.log("哈哈");
    }

}

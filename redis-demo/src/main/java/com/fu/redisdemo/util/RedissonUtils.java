package com.fu.redisdemo.util;

import com.fu.redisdemo.config.LogicFunction;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * Redisson工具类
 */
public final class RedissonUtils {
    //私有构造函数
    private RedissonUtils() {}

    //参数校验
    private static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 自动加锁和释放锁公共方法
     *
     * @param redisson RedissonClient
     * @param lockName 分布式锁的名称
     * @param logicFunction 业务逻辑函数
     * @return 是否加锁成功
     */
    public static boolean lock(RedissonClient redisson, String lockName, LogicFunction logicFunction) {
        // 参数校验
        notNull(redisson, "redisson must not be null");
        notNull(lockName, "lockName must not be null");
        notNull(logicFunction, "LogicFunction must not be null");

        RLock lock = redisson.getLock(lockName);
        boolean tryLock = lock.tryLock();

        //没有获取到锁，直接返回
        if (!tryLock) {
            return false;
        }

        try {
            //业务逻辑
            logicFunction.execute();
            return true;
        } catch (Exception e) {
            // 直接抛出原始异常（RuntimeException）
            throw (RuntimeException) e;
        } finally {
            // 安全释放锁（避免在锁已自动释放时抛出IllegalMonitorStateException）
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
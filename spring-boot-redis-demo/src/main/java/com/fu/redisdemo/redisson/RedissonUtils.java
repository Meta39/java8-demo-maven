package com.fu.redisdemo.redisson;

import com.fu.redisdemo.redisson.entity.RedissonResult;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Redisson工具类
 */
@Component
public final class RedissonUtils implements ApplicationContextAware {
    private static RedissonClient redissonClient;

    /**
     * 不等待锁
     */
    private static final long NO_WAIT_TIME = 0L;
    /**
     * 永不超时（一直阻塞，直到手动释放）
     */
    private static final long NO_LEASE_TIME = -1L;
    /**
     * 默认获取锁的等待时间
     */
    private static final long DEFAULT_WAIT_TIME = 5L;
    /**
     * 默认释放业务方法锁的超时时间
     */
    private static final long DEFAULT_LEASE_TIME = 60L;

    private RedissonUtils() {

    }

    /**
     * 启动完成的时候从 Spring 容器获取一次即可，防止多次获取。
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RedissonUtils.redissonClient = applicationContext.getBean(RedissonClient.class);
    }

    //================================ Runnable （返回 Boolean 是否获取到锁）================================

    /**
     * 获取锁（等待）并执行返回是否获取到锁的业务（短时）逻辑
     */
    public static boolean runnableLockWaitingShortTask(String lockKey, Runnable runnable) {
        return runnableLock(lockKey, DEFAULT_WAIT_TIME, DEFAULT_LEASE_TIME, runnable);
    }

    /**
     * 获取锁（等待）并执行返回是否获取到锁的业务（长时）逻辑
     */
    public static boolean runnableLockWaitingLongTask(String lockKey, Runnable runnable) {
        return runnableLock(lockKey, DEFAULT_WAIT_TIME, NO_LEASE_TIME, runnable);
    }

    /**
     * 获取锁（不等待）并执行返回是否获取到锁的业务（短时）逻辑
     */
    public static boolean runnableLockNoWaitingShortTask(String lockKey, Runnable runnable) {
        return runnableLock(lockKey, NO_WAIT_TIME, DEFAULT_LEASE_TIME, runnable);
    }

    /**
     * 获取锁（不等待）并执行返回是否获取到锁的业务（长时）逻辑
     */
    public static boolean runnableLockNoWaitingLongTask(String lockKey, Runnable runnable) {
        return runnableLock(lockKey, NO_WAIT_TIME, NO_LEASE_TIME, runnable);
    }

    /**
     * 获取锁并执行返回是否加锁成功的业务逻辑
     */
    public static boolean runnableLock(String lockKey, long waitTime, long leaseTime, Runnable runnable) {
        // 参数校验
        Assert.notNull(runnable, "runnable must not be null");
        RedissonResult<Object> objectRedissonResult = commonLogic(lockKey, waitTime, leaseTime, null, runnable);
        return objectRedissonResult.getLock();
    }

    //================================ Callable（返回RedissonResult） ================================

    /**
     * 获取锁（等待）并执行返回 RedissonResult 的业务（短时）逻辑
     */
    public static <T> RedissonResult<T> callableLockWaitingShortTask(String lockKey, Callable<T> callable) {
        return callableLock(lockKey, DEFAULT_WAIT_TIME, DEFAULT_LEASE_TIME, callable);
    }

    /**
     * 获取锁（等待）并执行返回 RedissonResult 的业务（长时）逻辑
     */
    public static <T> RedissonResult<T> callableLockWaitingLongTask(String lockKey, Callable<T> callable) {
        return callableLock(lockKey, DEFAULT_WAIT_TIME, NO_LEASE_TIME, callable);
    }

    /**
     * 获取锁（不等待）并执行返回 RedissonResult 的业务（短时）逻辑
     */
    public static <T> RedissonResult<T> callableLockNoWaitingShortTask(String lockKey, Callable<T> callable) {
        return callableLock(lockKey, NO_WAIT_TIME, DEFAULT_LEASE_TIME, callable);
    }

    /**
     * 获取锁（不等待）并执行返回 RedissonResult 的业务（长时）逻辑
     */
    public static <T> RedissonResult<T> callableLockNoWaitingLongTask(String lockKey, Callable<T> callable) {
        return callableLock(lockKey, NO_WAIT_TIME, NO_LEASE_TIME, callable);
    }

    /**
     * 获取锁并执行返回 RedissonResult 的业务逻辑
     */
    public static <T> RedissonResult<T> callableLock(String lockKey, long waitTime, long leaseTime, Callable<T> callable) {
        // 参数校验
        Assert.notNull(callable, "callable must not be null");
        return commonLogic(lockKey, waitTime, leaseTime, callable, null);
    }

    //================================ Callable 和 Runnable 通用核心逻辑 ================================

    /**
     * Callable 和 Runnable 通用核心逻辑
     *
     * @param lockKey   锁的Key
     * @param waitTime  获取锁等待时间(秒)
     * @param leaseTime 锁持有时间(秒)
     * @param callable  业务逻辑
     * @param runnable  业务逻辑
     * @param <T>       返回值类型
     */
    private static <T> RedissonResult<T> commonLogic(String lockKey, long waitTime, long leaseTime, Callable<T> callable, Runnable runnable) {
        //校验
        Assert.hasText(lockKey, "lockKey must have text; it must not be null, empty, or blank");
        Assert.notNull(redissonClient, "RedissonClient not initialized!");//未初始化 RedissonClient 就使用检测

        RLock lock = redissonClient.getLock(lockKey);
        try {
            //1.尝试加锁
            boolean tryLock = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
            //2.在指定时间内没有获取到锁直接返回，让使用方决定没有获取到锁如何操作，而不是抛异常。
            RedissonResult<T> redissonResult = new RedissonResult<>();
            if (!tryLock) {
                redissonResult.setLock(false);
                return redissonResult;
            }

            //3.获取到锁执行业务逻辑
            redissonResult.setLock(true);
            //3.1执行 runnable
            if (Objects.nonNull(runnable)) {
                runnable.run();
                return redissonResult;
            }
            //3.2执行 callable
            redissonResult.setData(callable.call());
            return redissonResult;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("RedissonUtils InterruptedException:" + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //锁不为空，并且是当前前程和已经上锁才解锁，防止解其它线程的锁。
            if (Objects.nonNull(lock) && lock.isHeldByCurrentThread() && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

}
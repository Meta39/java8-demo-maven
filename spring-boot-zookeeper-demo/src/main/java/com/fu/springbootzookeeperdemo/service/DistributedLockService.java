package com.fu.springbootzookeeperdemo.service;

import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DistributedLockService {
    private final CuratorFramework curatorFramework;

    private static final String LOCK_PATH = "/distributed-lock";

    /**
     * 执行带有分布式锁的任务
     * @param task 需要同步执行的业务逻辑
     */
    public void executeWithLock(Runnable task) {
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, LOCK_PATH);
        try {
            // 尝试获取锁，最多等待10秒
            if (lock.acquire(10, TimeUnit.SECONDS)) {
                try {
                    task.run(); // 执行临界区代码
                } finally {
                    lock.release(); // 确保锁被释放
                }
            } else {
                throw new RuntimeException("获取分布式锁失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("分布式锁操作异常", e);
        }
    }
}
package com.fu.springbootdemo.config;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public enum TaskRejectedExecutionHandler {
    AbortPolicy,
    CallerRunsPolicy,
    DiscardPolicy,
    DiscardOldestPolicy,
    ;

    /**
     * 拒绝处理策略
     * AbortPolicy()：默认策略，直接丢弃并抛出异常。
     * DiscardPolicy()：直接丢弃，不抛出异常。
     * DiscardOldestPolicy()：丢弃队列中最早的任务，腾出此线程，将等待的任务放入此线程。
     * CallerRunsPolicy()：如果线程池满了，则交由主线程执行。
     */
    public static RejectedExecutionHandler getRejectionHandler(TaskRejectedExecutionHandler rejectionPolicy) {
        switch (rejectionPolicy) {
            case AbortPolicy:
                return new ThreadPoolExecutor.AbortPolicy();
            case CallerRunsPolicy:
                return new ThreadPoolExecutor.CallerRunsPolicy();
            case DiscardPolicy:
                return new ThreadPoolExecutor.DiscardPolicy();
            case DiscardOldestPolicy:
                return new ThreadPoolExecutor.DiscardOldestPolicy();
            default:
                return new ThreadPoolExecutor.AbortPolicy();
        }
    }

}

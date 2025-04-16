package com.fu.basedemo.designpattern.singleton.usecase.async;

import java.util.concurrent.*;

public class AsyncExecutor {

    private AsyncExecutor() {
    }

    private static final int cpuCores = Runtime.getRuntime().availableProcessors();//CPU核心数
    private static final int corePoolSize = 2 * cpuCores;//IO密集型 corePoolSize = CPU核心数 * 2
    private static final int maximumPoolSize = corePoolSize * 5;//IO密集型 maximumPoolSize = CPU核心数 * 2 * n（n是一个较大的值，比如5-10）

    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1000);//队列大小 = (预计峰值任务数 / 平均任务处理时间) * 平均IO等待时间

    /**
     * IO密集型异步线程池
     */
    private static final ExecutorService executorService = new ThreadPoolExecutor(
            corePoolSize, //corePoolSize线程池基础线程大小。
            maximumPoolSize, //maximumPoolSize线程池最大线程数量。
            10L, //keepAliveTime线程池空闲线程存活时间
            TimeUnit.SECONDS, //TimeUnit线程池空闲线程存活时间keepAliveTime的时间单位，这里设置的单位是秒s
            queue, //BlockingQueue线程池工作队列和长度。ArrayBlockingQueue是有界的数组队列。建议：如果业务量很大，建议使用ArrayBlockingQueue。
            Executors.defaultThreadFactory(), //threadFactory设置创建线程的工厂
            /*
              拒绝处理策略
              AbortPolicy()：默认策略，直接丢弃并抛出异常。如果业务量很大，建议使用此策略抛出异常，捕获异常，对异常进行处理。如：记录日志或持久化不能处理的任务。
              DiscardPolicy()：直接丢弃，不抛出异常。
              DiscardOldestPolicy()：丢弃队列中最早的任务，腾出此线程，将等待的任务放入此线程。
              CallerRunsPolicy()：如果线程池满了，则交由主线程执行。建议：确保业务量不大并且系统硬件资源足够支持业务的情况下，可以使用CallerRunsPolicy。
             */
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    //使用的时候获取
    public static ExecutorService getInstance() {
        return executorService;
    }

    //停止应用程序的时候关闭
    public static void shutdown() {
        executorService.shutdown();
    }

}


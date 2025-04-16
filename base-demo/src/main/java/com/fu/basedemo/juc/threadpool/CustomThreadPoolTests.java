package com.fu.basedemo.juc.threadpool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * 自定义线程池（推荐）
 * 可以根据业务量去定制线程池的具体大小
 */
public class CustomThreadPoolTests {

    //自定义线程池。建议：应当根据系统硬件去设置具体的大小，太大可能导致OOM，太小无法充分利用系统资源。
    private static final ExecutorService customThreadPool = new ThreadPoolExecutor(
            8, //corePoolSize线程池基础线程大小。
            16, //maximumPoolSize线程池最大线程数量。
            60L, //keepAliveTime线程池空闲线程存活时间
            TimeUnit.SECONDS, //TimeUnit线程池空闲线程存活时间keepAliveTime的时间单位，这里设置的单位是秒s
//            new ArrayBlockingQueue<>(1000), //BlockingQueue线程池工作队列和长度。ArrayBlockingQueue是有界的数组队列。建议：如果业务量很大，建议使用ArrayBlockingQueue。
            new LinkedBlockingQueue<>(), //BlockingQueue线程池工作队列。LinkedBlockingQueue是无界的链表队列，可能会堆积大量的请求，从而导致 OOM。建议：确保业务量不大并且系统硬件资源足够支持业务的情况下，可以使用LinkedBlockingQueue
            Executors.defaultThreadFactory(), //threadFactory设置创建线程的工厂
            /*
              拒绝处理策略
              AbortPolicy()：默认策略，直接丢弃并抛出异常。如果业务量很大，建议使用此策略抛出异常，捕获异常，对异常进行处理。如：记录日志或持久化不能处理的任务。
              DiscardPolicy()：直接丢弃，不抛出异常。
              DiscardOldestPolicy()：丢弃队列中最早的任务，腾出此线程，将等待的任务放入此线程。
              CallerRunsPolicy()：如果线程池满了，则交由主线程执行。建议：确保业务量不大并且系统硬件资源足够支持业务的情况下，可以使用CallerRunsPolicy。
             */
            new ThreadPoolExecutor.CallerRunsPolicy()); //handler拒绝策略，即：线程池里的线程满了，后面新进来的请求如何处理。

    //自定义线程池
    @Test
    public void test() {
        System.out.println("自定义线程池");
        try {
            for (int i = 0; i <= 20; i++) {
                //执行
                customThreadPool.execute(() -> System.out.println(Thread.currentThread().getName() + "号窗口办理业务"));
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            //关闭
            customThreadPool.shutdown();
        }
    }

}

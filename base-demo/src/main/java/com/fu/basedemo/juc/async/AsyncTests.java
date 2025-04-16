package com.fu.basedemo.juc.async;

import com.fu.basedemo.designpattern.singleton.usecase.LoggerAsync;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * 异步调用：分为有返回值和无返回值
 */
public class AsyncTests {
    private static final int cpuCores = Runtime.getRuntime().availableProcessors();//CPU核心数
    private static final int corePoolSize = 2 * cpuCores;//IO密集型 corePoolSize = CPU核心数 * 2
    private static final int maximumPoolSize = corePoolSize * 5;//IO密集型 maximumPoolSize = CPU核心数 * 2 * n（n是一个较大的值，比如5-10）
    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1000);//队列大小 = (预计峰值任务数 / 平均任务处理时间) * 平均IO等待时间
    private static final ExecutorService customThreadPool = new ThreadPoolExecutor(
            corePoolSize, //corePoolSize线程池基础线程大小。
            maximumPoolSize, //maximumPoolSize线程池最大线程数量。
            10L, //keepAliveTime线程池空闲线程存活时间
            TimeUnit.SECONDS, //TimeUnit线程池空闲线程存活时间keepAliveTime的时间单位，这里设置的单位是秒s
            queue, //BlockingQueue线程池工作队列和长度。ArrayBlockingQueue是有界的数组队列。建议：如果业务量很大，建议使用ArrayBlockingQueue。
            Executors.defaultThreadFactory(), //threadFactory设置创建线程的工厂
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    //自定义静态方法简化 无返回值 + 使用自定义线程池
    public static void runAsyncTask(Runnable runnable){
        CompletableFuture.runAsync(
                runnable,
                customThreadPool
        );
    }

    //无返回值
    @Test
    public void test() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> asyncVoid = CompletableFuture.runAsync(() -> System.out.println(Thread.currentThread().getName() + "无返回值的异步调用"));
        asyncVoid.get();
    }

    //无返回值 + 使用自定义线程池
    @Test
    public void testAsync() {
        LoggerAsync log = LoggerAsync.getInstance();
        AsyncTests.runAsyncTask(() -> log.log("无返回值 + 使用自定义异步线程池"));
    }

    //有返回值
    @Test
    public void test2() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> asyncReturn = CompletableFuture.supplyAsync(() ->{
            System.out.println(Thread.currentThread().getName() + "有返回值的异步调用");
            //模拟异常
//            int i = 1/0;
            return 1;
        });
        //value是返回值，exception是抛出异常。value和exception互斥。
        asyncReturn.whenComplete((value,exception) ->{
            System.out.println("t======="+value);
            System.out.println("u======="+exception);
        }).get();
    }
}

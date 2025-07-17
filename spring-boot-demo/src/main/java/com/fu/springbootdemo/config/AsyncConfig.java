package com.fu.springbootdemo.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Async异步方法配置类
 * 1、异步方法必须是public void
 * 2、确保执行方法和异步执行的方法不在同一个类，如需在同类，需要手动代理。
 */
@EnableAsync //开启异步线程注解
@EnableScheduling //开启定时任务注解
@Slf4j
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.task.execution.pool") // 该注解的locations已经被启用，现在只要是在环境中，都会优先加载
public class AsyncConfig implements AsyncConfigurer {
    private int coreSize = 10;//设置默认值，如果yml配置文件有值，则赋值，否则使用默认值。
    private int maxSize = 50;
    private int keepAlive = 60;
    private int queueCapacity = 100;//队列容量不要设置太大，否则会影响效率。
    private String threadNamePrefixAsyncTask = "asyncTask";
    private String threadNamePrefixSchedulerTask = "schedulerTask";

    /**
     * 修改默认的 Async 异步线程池配置
     * 注意：异步线程池和定时任务线程池的最大线程池大小是大小相同，但不是公用一个线程池，他们是互相独立的。
     * 只是线程池大小设置成了一样而已。
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreSize);//默认线程池大小
        executor.setMaxPoolSize(maxSize);//最大线程池大小
        executor.setKeepAliveSeconds(keepAlive);//线程池存活时间单位秒s
        executor.setQueueCapacity(queueCapacity);//队列大小
        executor.setThreadNamePrefix(threadNamePrefixAsyncTask);
        /*
          拒绝处理策略
          AbortPolicy()：默认策略，直接丢弃并抛出异常。
          DiscardPolicy()：直接丢弃，不抛出异常。
          DiscardOldestPolicy()：丢弃队列中最早的任务，腾出此线程，将等待的任务放入此线程。
          CallerRunsPolicy()：如果线程池满了，则交由主线程执行。
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        log.info("异步线程池大小={}", maxSize);
        return executor;
    }

    /**
     * 定时任务线程池配置
     */
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(maxSize);
        scheduler.setThreadNamePrefix(threadNamePrefixSchedulerTask);
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        scheduler.initialize();
        log.info("定时任务线程池大小={}", maxSize);
        return scheduler;
    }

}

package com.fu.springbootdemo.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Async异步方法配置类
 * 1、异步方法必须是public void
 * 2、确保执行方法和异步执行的方法不在同一个类，如需在同类，需要手动代理。
 */
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.task.execution.pool") // 该注解的locations已经被启用，现在只要是在环境中，都会优先加载
public class AsyncConfig {
    private int coreSize = 8;//设置默认值，如果yml配置文件有值，则赋值，否则使用默认值。
    private int maxSize = 16;
    private int keepAlive = 60;
    private int queueCapacity = 1000;

    @Bean("async")
    public Executor async() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreSize);//默认线程池大小
        executor.setMaxPoolSize(maxSize);//最大线程池大小
        executor.setKeepAliveSeconds(keepAlive);//线程池存活时间单位秒s
        executor.setQueueCapacity(queueCapacity);//队列大小
        /*
          拒绝处理策略
          AbortPolicy()：默认策略，直接丢弃并抛出异常。
          DiscardPolicy()：直接丢弃，不抛出异常。
          DiscardOldestPolicy()：丢弃队列中最早的任务，腾出此线程，将等待的任务放入此线程。
          CallerRunsPolicy()：如果线程池满了，则交由主线程执行。
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}

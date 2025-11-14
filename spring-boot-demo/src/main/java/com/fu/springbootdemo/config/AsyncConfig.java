package com.fu.springbootdemo.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Setter
@Getter
@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {
    @Value("${spring.task.execution.rejection:CallerRunsPolicy}")
    private TaskRejectedExecutionHandler rejectionPolicy;
    private final TaskExecutionProperties taskExecutionProperties;

    @Override
    public Executor getAsyncExecutor() {
        TaskExecutionProperties.Pool pool = taskExecutionProperties.getPool();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(pool.getCoreSize());//默认线程池大小
        executor.setMaxPoolSize(pool.getMaxSize());//最大线程池大小
        executor.setKeepAliveSeconds((int) pool.getKeepAlive().getSeconds());//线程池存活时间单位秒s
        executor.setQueueCapacity(pool.getQueueCapacity());//队列大小
        executor.setThreadNamePrefix(taskExecutionProperties.getThreadNamePrefix());
        executor.setRejectedExecutionHandler(TaskRejectedExecutionHandler.getRejectionHandler(rejectionPolicy));
        executor.initialize();
        log.warn("AsyncExecutor pool max size = {},ThreadNamePrefix = {},RejectedExecutionHandler = {}",
                executor.getMaxPoolSize(),
                executor.getThreadNamePrefix(),
                executor.getThreadPoolExecutor().getRejectedExecutionHandler().getClass().getSimpleName()
        );
        return executor;
    }

}

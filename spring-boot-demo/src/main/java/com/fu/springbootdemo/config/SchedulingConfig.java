package com.fu.springbootdemo.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Slf4j
@Setter
@Getter
@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class SchedulingConfig implements SchedulingConfigurer {
    @Value("${spring.task.scheduling.rejection:AbortPolicy}")
    private TaskRejectedExecutionHandler rejectionPolicy;
    private final TaskSchedulingProperties taskSchedulingProperties;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        TaskSchedulingProperties.Pool pool = taskSchedulingProperties.getPool();
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(pool.getSize());
        scheduler.setThreadNamePrefix(taskSchedulingProperties.getThreadNamePrefix());
        scheduler.setRejectedExecutionHandler(TaskRejectedExecutionHandler.getRejectionHandler(rejectionPolicy));
        scheduler.setErrorHandler(t -> log.error("ScheduledTask error:", t));
        scheduler.initialize();
        taskRegistrar.setTaskScheduler(scheduler);
        log.warn("ScheduledTask pool size = {},ThreadNamePrefix = {},RejectedExecutionHandler = {}",
                pool.getSize(),
                scheduler.getThreadNamePrefix(),
                scheduler.getScheduledThreadPoolExecutor().getRejectedExecutionHandler().getClass().getSimpleName()
        );
    }

}

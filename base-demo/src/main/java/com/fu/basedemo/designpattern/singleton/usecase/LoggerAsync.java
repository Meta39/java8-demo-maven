package com.fu.basedemo.designpattern.singleton.usecase;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

/**
 * 单例模式异步日志记录
 */
public class LoggerAsync {
    private static final PrintStream out = System.out;
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ExecutorService executorService;

    // 饿汉式单例初始化
    static {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        int corePoolSize = 2 * cpuCores;
        int maximumPoolSize = corePoolSize * 5;
        int queueSize = 1000;

        executorService = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                10L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueSize),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    private LoggerAsync() {
        // 私有构造函数，防止外部实例化
    }

    private static final class LoggerHolder {
        private static final LoggerAsync INSTANCE = new LoggerAsync();
    }

    public static LoggerAsync getInstance() {
        return LoggerHolder.INSTANCE;
    }

    public void log(String message) {
        String timestamp = dateFormat.format(LocalDateTime.now());
        executorService.execute(() -> out.println(timestamp + " - " + message));
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("ExecutorService did not terminate");
                }
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
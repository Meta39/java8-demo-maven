package com.fu.basedemo.juc.util;

import com.fu.basedemo.juc.threadpool.CustomThreadPoolTests;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 并行批处理工具类
 */
public final class BatchParallelProcessorUtils {
    /**
     * 默认每批处理数据的数量
     */
    private static final int DEFAULT_BATCH_SIZE = 1000;

    /**
     * 默认最大并发批次数
     */
    private static final int DEFAULT_MAX_CONCURRENT_BATCHES = 10;

    /**
     * 默认获取全部结果的超时时间（秒）
     */
    private static final long DEFAULT_TIMEOUT = 5 * 60L;

    private BatchParallelProcessorUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * 批处理（使用默认参数）
     *
     * @param dataList       数据集合
     * @param batchProcessor 批处理方法
     * @param <T>            数据类型
     * @param <R>            结果类型
     * @return 结果集合
     */
    public static <T, R> List<R> parallelProcess(List<T> dataList, Function<List<T>, R> batchProcessor) {
        return parallelProcess(dataList, DEFAULT_BATCH_SIZE, batchProcessor);
    }

    /**
     * 批处理（指定批次大小）
     *
     * @param dataList       数据集合
     * @param batchSize      每批处理数据的数量
     * @param batchProcessor 批处理方法
     * @param <T>            数据类型
     * @param <R>            结果类型
     * @return 结果集合
     */
    public static <T, R> List<R> parallelProcess(List<T> dataList,
                                                 int batchSize,
                                                 Function<List<T>, R> batchProcessor) {
        return parallelProcess(dataList, batchSize, batchProcessor, DEFAULT_TIMEOUT);
    }

    /**
     * 批处理（指定批次大小和超时时间）
     *
     * @param dataList       数据集合
     * @param batchSize      每批处理数据的数量
     * @param batchProcessor 批处理方法
     * @param timeout        获取全部结果的超时时间（秒）
     * @param <T>            数据类型
     * @param <R>            结果类型
     * @return 结果集合
     */
    public static <T, R> List<R> parallelProcess(List<T> dataList,
                                                 int batchSize,
                                                 Function<List<T>, R> batchProcessor,
                                                 long timeout) {
        return parallelProcess(dataList, batchSize, batchProcessor, timeout, null);
    }

    /**
     * 批处理（指定批次大小、超时时间和线程池）
     *
     * @param dataList       数据集合
     * @param batchSize      每批处理数据的数量
     * @param batchProcessor 批处理方法
     * @param timeout        获取全部结果的超时时间（秒）
     * @param executor       自定义线程池，如果为null则使用默认线程池
     * @param <T>            数据类型
     * @param <R>            结果类型
     * @return 结果集合
     */
    public static <T, R> List<R> parallelProcess(List<T> dataList,
                                                 int batchSize,
                                                 Function<List<T>, R> batchProcessor,
                                                 long timeout,
                                                 Executor executor) {
        return parallelProcess(dataList, batchSize, batchProcessor, timeout, executor, DEFAULT_MAX_CONCURRENT_BATCHES);
    }

    /**
     * 完整的批处理方法（所有参数可配置）
     *
     * @param dataList             数据集合
     * @param batchSize            每批处理数据的数量
     * @param batchProcessor       批处理方法
     * @param timeout              获取全部结果的超时时间（秒）
     * @param executor             自定义线程池，如果为null则使用默认线程池
     * @param maxConcurrentBatches 最大并发批次数
     * @param <T>                  数据类型
     * @param <R>                  结果类型
     * @return 结果集合
     */
    public static <T, R> List<R> parallelProcess(List<T> dataList,
                                                 int batchSize,
                                                 Function<List<T>, R> batchProcessor,
                                                 long timeout,
                                                 Executor executor,
                                                 int maxConcurrentBatches
    ) {
        if (dataList.isEmpty()
                || batchProcessor == null
                || batchSize <= 0
                || batchSize > maxConcurrentBatches
                || timeout <= 0L
        ) {
            System.err.println("Parallel process requires at least 1 data or batch size.");
            return Collections.emptyList();
        }

        // 如果未提供线程池，使用默认线程池
        Executor actualExecutor = (executor != null) ? executor : getDefaultExecutor();

        // 根据数据集大小和并发限制选择处理策略

        return processAllConcurrently(dataList, batchSize, batchProcessor, timeout, actualExecutor);
    }

    /**
     * 处理所有批次（适合数据集较小的情况）
     * 如果返回的数据是空的，则说明这一批次执行失败了。
     */
    private static <T, R> List<R> processAllConcurrently(List<T> dataList,
                                                         int batchSize,
                                                         Function<List<T>, R> batchProcessor,
                                                         long timeout,
                                                         Executor executor) {
        // 1. 数据分批次
        List<List<T>> batches = splitIntoBatches(dataList, batchSize);

        // 2. 创建并行任务（需要使用自定义线程池）
        List<CompletableFuture<R>> futures = batches.stream()
                .map(batch -> CompletableFuture.supplyAsync(
                        () -> {
                            try {
                                return batchProcessor.apply(batch);
                            } catch (Exception ex) {
                                System.err.println(Thread.currentThread().getName() + ex.getMessage());
                                return null;
                            }
                        },
                        executor
                ))
                .collect(Collectors.toList());

        // 4. 等待所有任务完成并收集结果
        return collectResults(futures, timeout);
    }

    /**
     * 收集结果
     */
    private static <R> List<R> collectResults(
            List<CompletableFuture<R>> futures,
            long timeout) {

        // 3. 等待所有任务完成并收集结果
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // 4. 组合结果
        CompletableFuture<List<R>> allResultsFuture = allFutures.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );

        try {
            // 5. 获取结果并设置超时
            return allResultsFuture.get(timeout, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            // 取消未完成的任务
            futures.forEach(future -> future.cancel(true));
            throw new RuntimeException("Batch processing timed out", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // 取消未完成的任务
            futures.forEach(future -> future.cancel(true));
            throw new RuntimeException("Batch processing interrupted", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Batch processing failed", e);
        }
    }

    /**
     * 数据分批次（优化版）
     */
    private static <T> List<List<T>> splitIntoBatches(List<T> dataList, int batchSize) {
        final int totalSize = dataList.size();
        final int batchCount = (totalSize + batchSize - 1) / batchSize;

        return IntStream.range(0, batchCount)
                .mapToObj(i -> {
                    int start = i * batchSize;
                    int end = Math.min(start + batchSize, totalSize);
                    return dataList.subList(start, end);
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取默认线程池
     */
    private static Executor getDefaultExecutor() {
        return CustomThreadPoolTests.THREAD_POOL_EXECUTOR;
    }

}
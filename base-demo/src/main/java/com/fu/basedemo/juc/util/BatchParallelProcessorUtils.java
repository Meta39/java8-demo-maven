package com.fu.basedemo.juc.util;

import com.fu.basedemo.juc.threadpool.CustomThreadPoolTests;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 并行批处理工具类
 */
public final class BatchParallelProcessorUtils {
    private static final Logger log = Logger.getLogger(BatchParallelProcessorUtils.class.getName());
    /**
     * 默认每批处理数据的数量
     */
    private static final int DEFAULT_BATCH_SIZE = 1000;

    /**
     * 默认获取全部结果的超时时间（秒）
     */
    private static final long DEFAULT_TIMEOUT = 5 * 60L;

    private BatchParallelProcessorUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * 基本的批处理方法（使用默认线程池、默认批大小、默认超时时间）
     *
     * @param dataList       数据集合（必填）
     * @param batchProcessor 批处理方法（必传）
     * @param <T>            数据类型
     * @param <T,            R>            结果类型
     * @return 结果集合
     */
    public static <T, R> List<TaskResult<T, R>> parallelProcess(List<T> dataList, Function<List<T>, R> batchProcessor) {
        return parallelProcess(null, dataList, DEFAULT_BATCH_SIZE, batchProcessor, DEFAULT_TIMEOUT);
    }

    /**
     * 批处理方法（使用默认线程池、指定批大小、默认超时时间）
     *
     * @param dataList       数据集合（必填）
     * @param batchSize      每批处理数据的数量
     * @param batchProcessor 批处理方法（必传）
     * @param <T>            数据类型
     * @param <T,            R>            结果类型
     * @return 结果集合
     */
    public static <T, R> List<TaskResult<T, R>> parallelProcess(List<T> dataList, int batchSize, Function<List<T>, R> batchProcessor) {
        return parallelProcess(null, dataList, batchSize, batchProcessor, DEFAULT_TIMEOUT);
    }

    /**
     * 批处理方法（使用默认线程池、默认批大小、指定超时时间）
     *
     * @param dataList       数据集合（必填）
     * @param batchProcessor 批处理方法（必传）
     * @param timeout        获取全部结果的超时时间[单位：秒]
     * @param <T>            数据类型
     * @param <T,            R>            结果类型
     * @return 结果集合
     */
    public static <T, R> List<TaskResult<T, R>> parallelProcess(List<T> dataList, Function<List<T>, R> batchProcessor, long timeout) {
        return parallelProcess(null, dataList, DEFAULT_BATCH_SIZE, batchProcessor, timeout);
    }

    /**
     * 批处理方法（使用默认线程池、指定批大小和超时时间）
     *
     * @param dataList       数据集合（必填）
     * @param batchSize      每批处理数据的数量
     * @param batchProcessor 批处理方法（必传）
     * @param timeout        获取全部结果的超时时间[单位：秒]
     * @param <T>            数据类型
     * @param <T,            R>            结果类型
     * @return 结果集合
     */
    public static <T, R> List<TaskResult<T, R>> parallelProcess(List<T> dataList, int batchSize, Function<List<T>, R> batchProcessor, long timeout) {
        return parallelProcess(null, dataList, batchSize, batchProcessor, timeout);
    }

    /**
     * 批处理方法（指定线程池、使用默认批大小和超时时间）
     *
     * @param executor       自定义线程池
     * @param dataList       数据集合（必填）
     * @param batchProcessor 批处理方法（必传）
     * @param <T>            数据类型
     * @param <T,            R>            结果类型
     * @return 结果集合
     */
    public static <T, R> List<TaskResult<T, R>> parallelProcess(Executor executor, List<T> dataList, Function<List<T>, R> batchProcessor) {
        return parallelProcess(executor, dataList, DEFAULT_BATCH_SIZE, batchProcessor, DEFAULT_TIMEOUT);
    }

    /**
     * 批处理方法（指定线程池、批大小，使用默认超时时间）
     *
     * @param executor       自定义线程池
     * @param dataList       数据集合（必填）
     * @param batchSize      每批处理数据的数量
     * @param batchProcessor 批处理方法（必传）
     * @param <T>            数据类型
     * @param <T,            R>            结果类型
     * @return 结果集合
     */
    public static <T, R> List<TaskResult<T, R>> parallelProcess(Executor executor, List<T> dataList, int batchSize, Function<List<T>, R> batchProcessor) {
        return parallelProcess(executor, dataList, batchSize, batchProcessor, DEFAULT_TIMEOUT);
    }

    /**
     * 批处理方法（指定线程池、超时时间，使用默认批大小）
     *
     * @param executor       自定义线程池
     * @param dataList       数据集合（必填）
     * @param batchProcessor 批处理方法（必传）
     * @param timeout        获取全部结果的超时时间[单位：秒]
     * @param <T>            数据类型
     * @param <T,            R>            结果类型
     * @return 结果集合
     */
    public static <T, R> List<TaskResult<T, R>> parallelProcess(Executor executor, List<T> dataList, Function<List<T>, R> batchProcessor, long timeout) {
        return parallelProcess(executor, dataList, DEFAULT_BATCH_SIZE, batchProcessor, timeout);
    }

    /**
     * 完整的批处理方法（所有参数可配置）
     *
     * @param executor       自定义线程池（可选），如果不传，则使用 getDefaultExecutor()
     * @param dataList       数据集合（必填）
     * @param batchSize      每批处理数据的数量（可选），如果不传，则使用 DEFAULT_BATCH_SIZE
     * @param batchProcessor 批处理方法（必传）
     * @param timeout        获取全部结果的超时时间[单位：秒]（可选），如果不传，则使用 DEFAULT_TIMEOUT
     * @param <T>            数据类型
     * @param <T,            R>            结果类型
     * @return 结果集合
     */
    public static <T, R> List<TaskResult<T, R>> parallelProcess(Executor executor, List<T> dataList, int batchSize, Function<List<T>, R> batchProcessor, long timeout) {
        if (dataList.isEmpty() || batchProcessor == null || batchSize <= 0 || timeout <= 0L) {
            log.warning("Batch parallel process has invalid parameters.");
            return Collections.emptyList();
        }

        // 如果未提供线程池，使用默认线程池
        if (executor == null) {
            executor = getDefaultExecutor();
        }

        return processAllConcurrently(executor, dataList, batchSize, batchProcessor, timeout);
    }

    /**
     * 处理所有批次（适合数据集较小的情况）
     * 如果返回的数据是空的，则说明这一批次执行失败了。
     */
    private static <T, R> List<TaskResult<T, R>> processAllConcurrently(Executor executor, List<T> dataList, int batchSize, Function<List<T>, R> batchProcessor, long timeout) {
        // 1. 数据分批次
        List<List<T>> batches = splitIntoBatches(dataList, batchSize);

        // 2. 创建并行任务（需要使用自定义线程池）
        List<CompletableFuture<TaskResult<T, R>>> futures = batches.stream()
                .map(batch -> CompletableFuture.supplyAsync(
                        () -> {
                            try {
                                return TaskResult.<T, R>ok(batchProcessor.apply(batch));
                            } catch (Exception e) {
                                //记录异常日志
                                TaskResult<T, R> failure = TaskResult.failure(batch, e);
                                log.log(Level.SEVERE, "parallelProcess UUID:" + failure.getUuid(), e);
                                return failure;
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
    private static <T, R> List<TaskResult<T, R>> collectResults(List<CompletableFuture<TaskResult<T, R>>> futures, long timeout) {

        // 3. 等待所有任务完成并收集结果
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // 4. 组合结果
        CompletableFuture<List<TaskResult<T, R>>> allResultsFuture = allFutures.thenApply(v ->
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
            //绝大数情况下都是运行时异常，所以如果是运行时异常，则直接强转，不用重新new。
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 数据分批次
     */
    public static <T> List<List<T>> splitIntoBatches(List<T> dataList, int batchSize) {
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
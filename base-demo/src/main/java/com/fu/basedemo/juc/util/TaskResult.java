package com.fu.basedemo.juc.util;

import com.fu.basedemo.utils.UuidUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 任务执行结果包装类
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskResult<T, R> {
    private final String uuid;
    private final Boolean success;
    private final T batch;
    private final R result;
    private final Exception exception;

    /**
     * 成功也记录这一批次的入参（用于全部取消）
     *
     * @param batch  当前批次数据
     * @param result 当前批次结果
     */
    public static <T, R> TaskResult<T, R> ok(T batch, R result) {
        return new TaskResult<>(UuidUtils.getUUID(), true, batch, result, null);
    }

    /**
     * 异常
     *
     * @param exception 异常信息（不包括Error）
     * @param batch     当前批次数据
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> TaskResult<List<T>, R> failure(List<T> batch, Exception exception) {
        return new TaskResult<>(UuidUtils.getUUID(), false, batch, null, exception);
    }
}

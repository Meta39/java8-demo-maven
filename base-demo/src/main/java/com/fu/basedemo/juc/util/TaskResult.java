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
    private final R result;
    private final Exception exception;
    private final T batch;

    public static <T, R> TaskResult<T, R> success(R result) {
        return new TaskResult<>(UuidUtils.getUUID(), true, result, null, null);
    }

    public static <T, R> TaskResult<List<T>, R> failure(Exception exception, List<T> data) {
        return new TaskResult<>(UuidUtils.getUUID(), false, null, exception, data);
    }
}

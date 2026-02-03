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
    //批次ID
    private final String uuid;
    //成功
    private final Boolean success;
    private final R result;//结果
    //失败
    private final List<T> batchData;//批次数据
    private final Exception exception;//异常信息

    public static <T, R> TaskResult<T, R> ok(R result) {
        return new TaskResult<>(UuidUtils.getUUID(), true, result, null, null);
    }

    public static <T, R> TaskResult<T, R> failure(List<T> batchData, Exception exception) {
        return new TaskResult<>(UuidUtils.getUUID(), false, null, batchData, exception);
    }
}

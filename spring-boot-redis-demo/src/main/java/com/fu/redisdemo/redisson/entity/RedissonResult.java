package com.fu.redisdemo.redisson.entity;

import lombok.Data;

/**
 * 带返回值的自定义参数操作结果对象，让使用方决定没有获取到锁如何操作。
 */
@Data
public class RedissonResult<T> {
    /**
     * 是否成功获取锁
     */
    private Boolean lock;
    /**
     * 业务逻辑返回的数据结果
     */
    private T data;
}

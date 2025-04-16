package com.fu.springbootreadwritesplittingdemo.config;

/**
 * 只读数据库（从库）
 * 创建日期：2024-05-29
 */
public enum SlaveDB {
    SLAVE;

    public static final SlaveDB[] VALUES = values();
    public static final int VALUES_LENGTH = VALUES.length;

    public static SlaveDB getByIndex(int index) {
        if (index < 0 || index >= VALUES_LENGTH) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + VALUES_LENGTH);
        }
        return VALUES[index];
    }
}

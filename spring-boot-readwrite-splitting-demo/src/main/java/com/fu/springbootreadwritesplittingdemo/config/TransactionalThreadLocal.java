package com.fu.springbootreadwritesplittingdemo.config;

/**
 * 创建日期：2024-05-30
 */
public class TransactionalThreadLocal {
    private static final ThreadLocal<Boolean> IS_TRANSACTION = new ThreadLocal<>();

    public static void set(boolean value) {
        IS_TRANSACTION.set(value);
    }
    public static boolean get() {
        //因为这里返回的是 boolean，因此不能返回null，如果返回null，会报空指针异常
        if (IS_TRANSACTION.get() == null) {
            return false;
        }
        return IS_TRANSACTION.get();
    }

    public static void remove() {
        IS_TRANSACTION.remove();
    }
}

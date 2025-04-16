package com.fu.basedemo.base;

import org.junit.jupiter.api.Test;

/**
 * String、StringBuffer、StringBuilder的区别
 */
public class JavaStringAndBufferAndBuilderTest {

    /**
     * 虽然StringBuilder线程不安全，但是绝大多数情况下建议用StringBuilder，对于需要线程安全的建议用stringBuffer
     * 速度：
     * StringBuilder > StringBuffer > String
     * 可变性：
     * String 不可变
     * StringBuffer 和 StringBuilder 可变
     * 线程安全：
     * String 不可变，因此是线程安全的
     * StringBuilder 不是线程安全的
     * StringBuffer 是线程安全的，内部使用 synchronized 进行同步
     */
    @Test
    public void javaStringAndBufferAndBuilder() {
        //这种写法等价于StringBuilder stringBuilder = new StringBuilder().append("a").append("b");
        String str = "a" + "b";

        //String拼接（线程安全，最慢，因为定义了2个变量）
        String str2;
        String a = "a";
        String b = "b";
        str2 = a + b;

        //StringBuffer拼接（线程安全，中，安全是因为synchronized锁）
        StringBuffer stringBuffer = new StringBuffer().append("a").append("b");

        //StringBuilder拼接（线程不安全，最快，不安全是因为没有锁）
        StringBuilder stringBuilder = new StringBuilder().append("a").append("b");
    }
}

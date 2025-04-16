package com.fu.basedemo.base;

/**
 * Error:
 * 用来表示 JVM 无法处理的错误，与代码编写者所执行的操作无关，正常情况下，不大可能出现的情况。如：OutOfMemoryError内存溢出
 * Exception：
 * 非运行时异常，继承Exception，编译不通过，需要程序员自行在方法()throws Exception，方法里面throw new Exception()。
 * 运行时异常，继承RuntimeException，编译通过，需要程序员自己在代码里面throw new RuntimeException()
 */
public class JavaException extends Exception {

    private static final long serialVersionUID = 801342581753517148L;

    public JavaException(){}

    public JavaException(String message){
        super(message);
    }

    public JavaException(String message, Throwable throwable){
        super(message,throwable);
    }

    public JavaException(Throwable throwable){
        super(throwable);
    }
}

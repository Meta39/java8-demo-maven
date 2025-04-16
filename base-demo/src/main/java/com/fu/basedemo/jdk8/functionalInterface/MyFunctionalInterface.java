package com.fu.basedemo.jdk8.functionalInterface;

/**
 * 声明此接口是函数式接口
 */
@FunctionalInterface
public interface MyFunctionalInterface<T> {

    T add(T obj1,T obj2);

}

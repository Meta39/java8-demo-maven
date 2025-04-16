package com.fu.springbootdemo.strategy;

/**
 * 1. 新增策略接口。
 * 应用场景：多端登录，不同的端使用不同的数据库，所以执行不同的策略。
 */
public interface Strategy<T> {
    //2. 定义策略抽象方法
    T login(String username,String password);
}

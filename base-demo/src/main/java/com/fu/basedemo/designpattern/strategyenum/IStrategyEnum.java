package com.fu.basedemo.designpattern.strategyenum;

/**
 * 策略接口
 */
public interface IStrategyEnum<T> {

    /**
     * 执行策略
     * @param type 类型
     */
    T execute(Integer type);
//    T execute(Object dto);//最简单的办法就是传入一个自定义的DTO扩展性会更好。

}

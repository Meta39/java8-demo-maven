package com.fu.redisdemo.config;

/**
 * 定义业务函数式接口
 */
@FunctionalInterface
public interface LogicFunction {
    
    /**
     * 业务逻辑
     */
    void execute();

}

package com.fu.springbootwebservicedemo.ws;

/**
 * 统一 post 调用
 * 创建日期：2024-07-01
 */
public interface IWebService<T> {

    /**
     * 如果实现的接口是无返回值 void，则返回 Void 对象
     */
    Object handle(T req);

}

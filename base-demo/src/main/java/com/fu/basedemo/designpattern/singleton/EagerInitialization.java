package com.fu.basedemo.designpattern.singleton;

import java.io.Serializable;

/**
 * 饿汉式（推荐，如果已经知道类一定会被使用的情况下，推荐使用饿汉式这种方式实现单例模式。）
 * Runtime类就是一个典型的饿汉式单例模式。
 * 优点：
 * 1、实现简单：代码简单直观，易于理解和实现。
 * 2、线程安全：在类加载时实例化，保证了线程安全性。
 * 缺点：
 * 1、资源浪费：实例在类加载时创建，即使在运行过程中可能从未被使用。
 * 2、不支持延迟初始化：无法实现懒加载，可能导致启动时的资源浪费。
 *
 * @since 2024-07-25
 */
public class EagerInitialization implements Serializable {
    private static final long serialVersionUID = 1L;

    //私有化构造函数，防止外部实例化。
    private EagerInitialization() {}

    private static final EagerInitialization INSTANCE = new EagerInitialization();

    //获取实例
    public static EagerInitialization getInstance() {
        return INSTANCE;
    }

}

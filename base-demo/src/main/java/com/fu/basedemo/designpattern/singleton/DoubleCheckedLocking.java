package com.fu.basedemo.designpattern.singleton;

import java.io.Serializable;

/**
 * 懒汉式双重检查锁定（推荐，适用于不确定是否会被使用的场景。）
 * 双重检查锁定是一种常见且高效的单例模式实现方法。它使用volatile关键字确保对象初始化的可见性，并减少了同步的开销。
 * 优点：
 * 1、线程安全：通过双重检查锁定来保证线程安全。
 * 2、懒加载：实例在需要时才创建，避免了不必要的资源浪费。
 * 3、性能优化：减少了同步的开销，提升了性能（与普通的同步方法相比）。
 * 缺点：
 * 1、复杂性高：实现复杂，需要正确处理 volatile 关键字以及避免指令重排序等问题。
 * 2、潜在问题：在某些 JVM 实现中，可能会有因指令重排序导致的潜在问题。
 * 3、性能开销：虽然优化了同步开销，但在创建实例时仍然需要进行同步操作。
 *
 * @since 2024-07-25
 */
public class DoubleCheckedLocking implements Serializable {
    private static final long serialVersionUID = 1L;
    private static volatile DoubleCheckedLocking instance;

    //私有化构造函数，防止外部实例化
    private DoubleCheckedLocking() {
    }

    //获取实例
    public static DoubleCheckedLocking getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckedLocking.class) {
                if (instance == null) {
                    instance = new DoubleCheckedLocking();
                }
            }
        }
        return instance;
    }

}
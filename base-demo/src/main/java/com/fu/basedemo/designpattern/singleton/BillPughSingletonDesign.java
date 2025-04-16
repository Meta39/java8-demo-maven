package com.fu.basedemo.designpattern.singleton;

import java.io.Serializable;

/**
 * 注意事项：
 * 懒汉式静态内部类（推荐，适用于不确定是否会被使用的场景。）
 * 这种方法利用了类加载机制，只有在第一次调用getInstance方法时，静态内部类才会被加载，从而实现懒加载。
 * 优点：
 * 1、线程安全：利用类加载机制保证线程安全。
 * 2、懒加载：实例仅在第一次使用时创建，避免了饿汉式单例的资源浪费。
 * 3、性能较好：不需要同步机制，性能开销小。
 * 缺点：
 * 1、复杂性：实现比饿汉式复杂，可能不够直观。
 * 2、与序列化相关的潜在问题：虽然静态内部类的实例化是线程安全的，但序列化时需要注意。
 *
 * @since 2024-07-25
 */
public class BillPughSingletonDesign implements Serializable {
    private static final long serialVersionUID = 1L;

    //私有化构造函数，防止外部实例化
    private BillPughSingletonDesign() {
        // 无法防止反射攻击，它一直为true.
        /*if (SingletonHolder.INSTANCE != null) {
            throw new IllegalStateException("Instance already exists");
        }*/
    }

    //静态内部类
    private static class SingletonHolder {
        private static final BillPughSingletonDesign INSTANCE = new BillPughSingletonDesign();
    }

    //获取实例
    public static BillPughSingletonDesign getInstance() {
        return SingletonHolder.INSTANCE;
    }

}

package com.fu.basedemo.designpattern.singleton;

import java.io.Serializable;

/**
 * 懒汉式双重检查锁定（带参数的懒汉式单例模式，推荐使用双重锁检查）
 * 1.一旦单例实例被创建，传入的参数将无法更改。如果需要动态修改参数，建议通过方法传参而不是构造函数。
 * 2.这种方式适用于初始化时需要参数，且参数在单例生命周期内不需要改变的场景。如：密码加密
 */
public class DoubleCheckedLockingParam implements Serializable {
    private static final long serialVersionUID = 1L;
    // 使用volatile关键字确保多线程环境下的可见性和禁止指令重排序
    private static volatile DoubleCheckedLockingParam instance;
    // 参数
    private final String username;
    private final String password;
    private final String version;

    // 私有构造方法，防止外部实例化
    private DoubleCheckedLockingParam(String username, String password, String version) {
        // 下面这个判断无法止反射攻击
        /*if (instance != null) {
            throw new RuntimeException("DoubleCheckedLockingParam 实例已创建");
        }*/
        this.username = username;
        this.password = password;
        this.version = version;
    }

    // 公共静态方法，提供全局访问点
    public static DoubleCheckedLockingParam getInstance(String username, String password, String version) {
        if (instance == null) {// 第一次检查（无需同步）
            synchronized (DoubleCheckedLockingParam.class) {
                if (instance == null) {// 第二次检查（同步）
                    instance = new DoubleCheckedLockingParam(username, password, version);
                }
            }
        }
        return instance;
    }

    @Override
    public String toString() {
        return "DoubleCheckedLockingParam{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
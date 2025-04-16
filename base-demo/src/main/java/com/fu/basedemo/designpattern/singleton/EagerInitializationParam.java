package com.fu.basedemo.designpattern.singleton;

import java.io.Serializable;

/**
 * 带参数饿汉式（推荐，如果已经知道类一定会被使用的情况下，推荐使用饿汉式这种方式实现单例模式。）
 */
public class EagerInitializationParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final EagerInitializationParam INSTANCE;
    //参数
    private final String dbUrl;

    /*
        静态块
        作用：静态块用于初始化类的静态成员变量或执行一些只需要执行一次的静态操作。它在类加载时执行，且只执行一次。
        执行时机：类加载时执行，早于任何对象的创建。
     */
    static {
        String dbUrl = "jdbc:mysql://localhost:3306/mydb"; // 可以通过配置文件或其他方式获取
        INSTANCE = new EagerInitializationParam(dbUrl);
    }

    //私有化构造函数，防止外部实例化。
    private EagerInitializationParam(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    //获取实例
    public static EagerInitializationParam getInstance() {
        return INSTANCE;
    }

    // 示例方法
    public void connect() {
        System.out.println("Connecting to database at " + dbUrl);
    }

}

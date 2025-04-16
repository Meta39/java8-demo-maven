package com.fu.basedemo.base;

public class CClass {
    public static int a = 1;
    public static int b = 2;
    public static CClass cClass = new CClass();
    public int c = 3;

    /*
        构造块
        作用：构造块用于初始化实例变量或执行一些每次创建对象时都需要执行的操作。它在每次创建对象时执行，且在构造函数之前执行。
        执行时机：每次创建对象时执行，早于构造函数。
     */
    {
        System.out.println("构造块：a=" + a + ",b=" + b + ",c=" + c);
    }

    /*
        构造器
        作用：构造函数用于初始化对象的状态。它在创建对象时调用，用于设置对象的初始值或执行必要的初始化操作。
        执行时机：每次创建对象时执行，且在构造块之后执行。
     */
    public CClass() {
        c = 4;
        System.out.println("构造器：a=" + a + ",b=" + b + ",c=" + c);
    }

    /*
        静态块
        作用：静态块用于初始化类的静态成员变量或执行一些只需要执行一次的静态操作。它在类加载时执行，且只执行一次。
        执行时机：类加载时执行，早于任何对象的创建。
     */
    static {
        //因为在创建对象之前执行，所以无法直接获取变量c。由于public static CClass cClass是静态方法，所以可以直接调用cClass.c。
        System.out.println("静态块：a=" + a + ",b=" + b + ",cClass.c=" + cClass.c);
    }

    public static void main(String[] args) {
        System.out.println("main方法new CClass");
        new CClass();
    }

}

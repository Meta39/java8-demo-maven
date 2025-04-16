package com.fu.basedemo.jdk8.functionalInterface;

public abstract class MyFunctionalInterfaceUtil {

    /**
     * add接口的具体实现方法1
     */
    public static String add(String a, String b) {
        //(str1, str2) -> str1 + str2就是函数式接口的具体实现方法
        MyFunctionalInterface<String> myFunctionalInterface = (str1, str2) -> str1 + str2;
        //然后传参调用函数式接口方法
        return myFunctionalInterface.add(a, b);
    }

    /**
     * add接口的具体实现方法2
     */
    public static int add(int a, int b) {
        MyFunctionalInterface<Integer> myFunctionalInterface = (i1, i2) -> i1 + i2;
        return myFunctionalInterface.add(a, b);
    }

}

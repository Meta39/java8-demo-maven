package com.fu.basedemo.base;

/**
 * 枚举类：调用Enums.XIAO_WANG.getName();
 */
public enum Enums {

    XIAO_WANG("小王"),
    ZHANG_SAN("张三"),
    LI_SI("李四");

    private final String name;

    Enums(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

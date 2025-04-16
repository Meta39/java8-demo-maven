package com.fu.basedemo.designpattern.strategyenum;

/**
 * 策略枚举类
 */
public enum StrategyEnum {

    //根据不同的类型执行不同的策略
    A(1, "AStrategyEnumImpl"),
    B(2, "BStrategyEnumImpl"),
    ;

    private final int type;//类型
    private final String value;//实现类名（此名称必须和反射名称相同且区分大小写）

    //根据类型返回该枚举类型
    public static StrategyEnum value(int type) {
        for(StrategyEnum strategyEnum : StrategyEnum.values()) {
            if (strategyEnum.getType() == type) {
                return strategyEnum;
            }
        }
        return null;
    }

    StrategyEnum(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}

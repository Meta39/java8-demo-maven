package com.fu.basedemo.designpattern.strategyenum;



import java.util.Objects;

/**
 * 根据传入的类型，通过反射创建相应的策略实现类。
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class CreateStrategy<T> {

    /**
     * 根据传入的类型执行不同的策略
     *
     * @param type 传入的类型
     */
    public T doExecute(Integer type) {
        Class<?> c;//反射
        IStrategyEnum<?> strategyEnum;
        try {
            StrategyEnum value = StrategyEnum.value(type);
            if (Objects.isNull(value)) {
                throw new RuntimeException("StrategyEnum不存在此枚举值type：" + type);
            }
            //通过反射获取对象，拼接反射对象的包名+反射对象的名字
            c = Class.forName("com.fu.basedemo.designpattern.strategyenum.impl." + value.getValue());
            strategyEnum = (IStrategyEnum<?>) c.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            log.error("反射异常：", e);
            throw new RuntimeException("反射异常");
        }
        return (T) strategyEnum.execute(StrategyEnum.value(type).getType());//执行具体的策略，也就是实现类。
    }

}

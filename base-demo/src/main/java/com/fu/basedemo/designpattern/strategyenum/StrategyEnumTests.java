package com.fu.basedemo.designpattern.strategyenum;

import org.junit.jupiter.api.Test;

public class StrategyEnumTests {

    @Test
    public void test() {
        /*
        因为这里默认每次只会执行一次策略，所以需要执行不同的策略需要new不同的CreateStrategy，
        而且StrategyEnumImpl的返回类型是泛型，因此每个impl实现类的返回类型可以是不同的，更加灵活。
         */
        CreateStrategy<String> createStrategy = new CreateStrategy<>();//策略A返回的类型是String
        System.out.println(createStrategy.doExecute(1));

        CreateStrategy<Integer> createStrategy2 = new CreateStrategy<>();//策略B返回的类型是Integer
        System.out.println(createStrategy2.doExecute(2));
    }

}

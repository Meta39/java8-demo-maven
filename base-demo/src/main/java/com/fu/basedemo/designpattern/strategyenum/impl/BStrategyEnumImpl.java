package com.fu.basedemo.designpattern.strategyenum.impl;

import com.fu.basedemo.designpattern.strategyenum.IStrategyEnum;

public class BStrategyEnumImpl implements IStrategyEnum<Integer> {

    @Override
    public Integer execute(Integer type) {
        return 2;
    }
}

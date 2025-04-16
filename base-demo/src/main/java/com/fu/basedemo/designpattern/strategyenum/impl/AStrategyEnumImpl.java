package com.fu.basedemo.designpattern.strategyenum.impl;

import com.fu.basedemo.designpattern.strategyenum.IStrategyEnum;

public class AStrategyEnumImpl implements IStrategyEnum<String> {

    @Override
    public String execute(Integer type) {
        return "策略A";
    }

}

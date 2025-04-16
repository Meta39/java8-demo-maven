package com.fu.basedemo.designpattern.adapterpattern;

// 适配器类
public class AdapterPattern implements NewInterface {
    private final OldInterface oldInterface;

    public AdapterPattern(OldInterface oldInterface) {
        this.oldInterface = oldInterface;
    }

    @Override
    public void newMethod() {
        // 调用旧接口的方法
        oldInterface.oldMethod();
    }

}


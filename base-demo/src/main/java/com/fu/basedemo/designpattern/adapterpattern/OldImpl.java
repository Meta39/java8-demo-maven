package com.fu.basedemo.designpattern.adapterpattern;


// 旧接口的实现类
public class OldImpl implements OldInterface {

    @Override
    public void oldMethod() {
        System.out.println("Old method implementation.");
    }

}

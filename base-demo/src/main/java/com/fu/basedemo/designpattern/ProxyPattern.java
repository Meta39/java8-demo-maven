package com.fu.basedemo.designpattern;

import org.junit.jupiter.api.Test;

/**
 * 代理模式：代理类和真实类必须实现同样的接口
 * 如：AOP
 */
//1.定义一个本体和代理都需要的接口
public interface ProxyPattern {
    void buy();
}

//2.本体实现接口
class NeedToBuyPerson implements ProxyPattern{

    @Override
    public void buy() {
        //人在国内想买一个美版的iphone
        System.out.println("买一个美版iphone 14 pro max");
    }
}

//3.代理实现接口
class ProxyPerson implements ProxyPattern{

    @Override
    public void buy() {
        //人在美国，从美国买了带回国内
        System.out.println("我是代购，我帮你买！");//像不像AOP@Before注解操作的内容
        //由代理去调用本体的接口实现
        ProxyPattern needToBuyPerson = new NeedToBuyPerson();
        needToBuyPerson.buy();
        System.out.println("回国");//像不像AOP@Around注解操作的内容
        System.out.println("交给购买者");//像不像AOP@After注解操作的内容
    }
}

//4.测试
class ProxyPatternTest{

    @Test
    public void test(){
        //调用代理，让代理去执行本体的方法
        ProxyPattern proxyPerson = new ProxyPerson();
        proxyPerson.buy();
    }

}
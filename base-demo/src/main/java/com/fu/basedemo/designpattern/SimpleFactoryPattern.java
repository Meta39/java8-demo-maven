package com.fu.basedemo.designpattern;

import org.junit.jupiter.api.Test;

/**
 * 简单工厂模式
 */
//1.创建一个接口，接口里面只有一个方法
public interface SimpleFactoryPattern {
    String factoryFunction();
}

//2.产品A实现简单工厂接口
class ProductA implements  SimpleFactoryPattern{

    @Override
    public String factoryFunction() {
        return "你选择的是产品A";
    }
}

//2.产品B实现简单工厂接口
class ProductB implements  SimpleFactoryPattern{

    @Override
    public String factoryFunction() {
        return "我是产品B，选我！";
    }
}

//3.创建enum枚举类
enum SimpleFactoryEnum{
    ProductA,ProductB,ProductC

}

//4.创建简单工厂类根据传参调用具体的产品
class SimpleFactory {

    //虽然返回值是抽象接口，但是内部方法是返回的实现接口类
    public static SimpleFactoryPattern getProduct(SimpleFactoryEnum productName){
        if (productName == SimpleFactoryEnum.ProductA){
            return new ProductA();
        } else if (productName == SimpleFactoryEnum.ProductB){
            return new ProductB();
        }
        throw new RuntimeException("没有此产品");
    }
}

//5.测试
class SimpleFactoryPatternTest{

    @Test
    public void test(){
        //调用产品A
        SimpleFactoryPattern productA = SimpleFactory.getProduct(SimpleFactoryEnum.ProductA);
        System.out.println(productA.factoryFunction());
        //调用产品B的方法
        SimpleFactoryPattern productB = SimpleFactory.getProduct(SimpleFactoryEnum.ProductB);
        System.out.println(productB.factoryFunction());
        //没有产品C
        SimpleFactoryPattern productC = SimpleFactory.getProduct(SimpleFactoryEnum.ProductC);
        System.out.println(productC.factoryFunction());
    }

}
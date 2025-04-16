package com.fu.basedemo.jdk8;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.function.Consumer;

public class LambdaTest {

    /**
     * 实例方法
     */
    @Test
    public void test1(){
        //原始写法
        Consumer<String> con1 = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        con1.accept("原始写法");

        //简化：lambda写法
        Consumer<String> con2 = s -> System.out.println(s);
        con2.accept("lambda写法");

        //进一步简化：方法引用
        Consumer<String> con3 = System.out::println;
        con3.accept("方法引用");
    }

    /**
     * 静态方法
     */
    @Test
    public void test2(){
        //原始写法
        Comparator<Integer> com1 = new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                return Integer.compare(i1,i2);
            }
        };
        System.out.println(com1.compare(1, 2));

        //lambda写法
        Comparator<Integer> com2 = (Integer i1,Integer i2) -> Integer.compare(i1,i2);
        System.out.println(com2.compare(1, 2));

        //方法引用
        Comparator<Integer> com3 = Integer::compare;
        System.out.println(com3.compare(1, 2));
    }

    /**
     * 类（比较难理解）
     */
    @Test
    public void test3(){
        //原始写法
        Comparator<String> com1 = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        };
        System.out.println(com1.compare("abc", "bcd"));

        //lambda写法
        Comparator<String> com2 = (String s1,String s2) -> s1.compareTo(s2);
        System.out.println(com2.compare("abc", "bcd"));

        //方法引用
        Comparator<String> com3 = String::compareTo;
        System.out.println(com3.compare("abc", "bcd"));
    }

}

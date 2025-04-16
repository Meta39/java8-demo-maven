package com.fu.basedemo.jdk8.functionalInterface;

import org.junit.jupiter.api.Test;

public class FunctionalInterfaceTest {

    @Test
    void sum() {
        System.out.println(MyFunctionalInterfaceUtil.add("a", "b"));
        System.out.println(MyFunctionalInterfaceUtil.add(1, 2));
    }

}

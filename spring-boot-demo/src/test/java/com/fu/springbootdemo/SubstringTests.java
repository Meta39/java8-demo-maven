package com.fu.springbootdemo;


import org.junit.jupiter.api.Test;

import java.util.UUID;

public class SubstringTests {
    @Test
    public void subStringTest(){
        String str = UUID.randomUUID().toString().replaceAll("-","") + 1;
        System.out.println(str.substring(32));
    }
}

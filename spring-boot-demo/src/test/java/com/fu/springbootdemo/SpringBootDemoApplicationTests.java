package com.fu.springbootdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
public class SpringBootDemoApplicationTests {

    @Test
    public void test(){
        String headSalt = "headSalt";//头部盐
        String tailSalt = "tailSalt";//尾部盐
        String saltPassword = headSalt + "password" +tailSalt;
        saltPassword = saltPassword.substring(headSalt.length());
        String password =saltPassword.substring(0,saltPassword.length() - tailSalt.length());
        System.out.println(password);
    }

}

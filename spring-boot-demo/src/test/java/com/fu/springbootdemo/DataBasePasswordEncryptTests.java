package com.fu.springbootdemo;

import com.fu.springbootdemo.util.RSAUtil;
import org.junit.jupiter.api.Test;

/**
 * 模拟对原始密码加密存放到数据库
 */
//@SpringBootTest
public class DataBasePasswordEncryptTests {

    @Test
    public void test(){
        String encryptPassword = RSAUtil.encrypt("user", "salt");
        String decryptPassword = RSAUtil.decrypt(encryptPassword, "salt");
        System.out.println(encryptPassword);
        System.out.println("user".equals(decryptPassword));
    }
}

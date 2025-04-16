package com.fu.springbootdemo;

import com.fu.springbootdemo.util.BCryptPasswordEncoderUtil;
import org.junit.jupiter.api.Test;

/**
 * Spring Security搬过来的BCryptPasswordEncoder
 */
public class BCryptPasswordEncoderUtilTests {

    @Test
    public void test() {
        BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil = new BCryptPasswordEncoderUtil();
        String password = "解密后的密码";//建议前端的加密使用非对称加盐加密，然后私钥解密。
        String encodePassword = bCryptPasswordEncoderUtil.encode(password);//后端把前端密文解密后，再次加密保存到数据库
        System.out.println(bCryptPasswordEncoderUtil.matches(password, encodePassword));//比对前端密文解密后的密码和数据库密文
    }

}

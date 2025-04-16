package com.fu.xxljobdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.StringUtils;

@SpringBootTest
public class XxlJobDemoApplicationTests {
    @Autowired
    PropertyResolver propertyResolver;

    @Test
    public void test(){
        String str = propertyResolver.getProperty("logging.file.path");
        System.out.println(StringUtils.hasText(str));
        System.out.println(str);
    }

}

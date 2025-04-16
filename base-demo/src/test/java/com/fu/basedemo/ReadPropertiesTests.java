package com.fu.basedemo;

import com.fu.basedemo.utils.PropertiesUtils;
import org.junit.jupiter.api.Test;

import java.util.Properties;

/**
 * 读取properties配置文件
 */
public class ReadPropertiesTests {

    @Test
    public void testProperties(){
        Properties testProperties = PropertiesUtils.getTestProperties();
        String param = testProperties.getProperty("params.app");
        System.out.println(param);
    }

}

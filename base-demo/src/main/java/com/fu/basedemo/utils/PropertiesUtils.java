package com.fu.basedemo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 读取 properties 配置文件工具类
 */
public class PropertiesUtils {
    /**
     * 私有化构造函数
     */
    private PropertiesUtils(){}
    /**
     * 路径配置错误会抛出空指针异常
     */
    private static final String TEST_PROPERTIES_URI = "/properties/test.properties";

    /**
     * 获取test.properties
     */
    public static Properties getTestProperties() {
        Properties props = new Properties();
        InputStream inputStream = PropertiesUtils.class.getResourceAsStream(TEST_PROPERTIES_URI);
        //*.properties配置文件，要使用UTF-8编码，否则会现中文乱码问题
        assert inputStream != null;
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        try {
            props.load(bf);
        } catch (IOException e) {
//            log.error("读取配置" + TEST_PROPERTIES_URI + "文件异常", e);
            throw new RuntimeException(e);
        }
        return props;
    }

}

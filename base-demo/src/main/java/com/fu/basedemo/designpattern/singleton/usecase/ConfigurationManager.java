package com.fu.basedemo.designpattern.singleton.usecase;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigurationManager {
    private static volatile ConfigurationManager instance;
    private final Properties properties;

    private ConfigurationManager() {
        properties = new Properties();
        // 加载配置文件
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("properties/test.properties")) {
            if (input == null) {
                throw new RuntimeException("Configuration file not found: properties/test.properties");
            }
            try (InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                // 加载属性文件
                properties.load(reader);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    private static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }

    public static String getProperty(String key) {
        return getInstance().properties.getProperty(key);
    }

}
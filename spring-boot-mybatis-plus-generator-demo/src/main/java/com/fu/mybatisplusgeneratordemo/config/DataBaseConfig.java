package com.fu.mybatisplusgeneratordemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = DataBaseConfig.ALL_PREFIX)
public class DataBaseConfig {
    public static final String PREFIX = "database";
    public static final String ALL_PREFIX = MyBatisPlusGeneratorConfig.PREFIX + MyBatisPlusGeneratorConfig.POINT + PREFIX;
    private String url;
    private String username = "root";
    private String password = "123456";
}

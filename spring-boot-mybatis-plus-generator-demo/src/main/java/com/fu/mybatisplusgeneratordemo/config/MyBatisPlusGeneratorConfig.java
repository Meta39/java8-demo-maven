package com.fu.mybatisplusgeneratordemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = MyBatisPlusGeneratorConfig.PREFIX)
public class MyBatisPlusGeneratorConfig {
    public static final String POINT = ".";
    public static final String PREFIX = "mybatis-plus-generator";
    private List<String> tables = Collections.emptyList();
    private String author = "zhongyf";
    private String outputDir = "D://";
    private String commentDate = "yyyy-MM-dd";
    private String parentPackage;
    private String entityPackageName = "entity";
    private String mapperPackageName = "mapper";
}

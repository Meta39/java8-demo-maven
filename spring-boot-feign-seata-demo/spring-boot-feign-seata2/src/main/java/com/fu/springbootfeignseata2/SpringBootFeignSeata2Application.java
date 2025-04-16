package com.fu.springbootfeignseata2;

import com.alibaba.cloud.seata.rest.SeataRestTemplateAutoConfiguration;
import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 创建日期：2024-05-13
 */
@MapperScan("com.fu.springbootfeignseata2.mapper")
// 被调用端引用 seata 默认 AT 模式
@EnableAutoDataSourceProxy
// seata 版本兼容性问题，如果启动报错就要排除 SeataRestTemplateAutoConfiguration 自动配置
@SpringBootApplication(exclude = SeataRestTemplateAutoConfiguration.class)
public class SpringBootFeignSeata2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootFeignSeata2Application.class, args);
    }

}
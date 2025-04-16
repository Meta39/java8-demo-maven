package com.fu.springbootfeignseata1;

import com.alibaba.cloud.seata.rest.SeataRestTemplateAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 创建日期：2024-05-13
 */
@MapperScan("com.fu.springbootfeignseata1.mapper")
@EnableFeignClients
// seata 版本兼容性问题，如果启动报错就要排除 SeataRestTemplateAutoConfiguration 自动配置
@SpringBootApplication(exclude = SeataRestTemplateAutoConfiguration.class)
public class SpringBootFeignSeata1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootFeignSeata1Application.class, args);
    }

}
package com.fu.springbootjta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//启用事务管理
@EnableTransactionManagement
//排除数据源自动配置，因为是2个数据源
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpringBootJTADemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJTADemoApplication.class, args);
    }

}
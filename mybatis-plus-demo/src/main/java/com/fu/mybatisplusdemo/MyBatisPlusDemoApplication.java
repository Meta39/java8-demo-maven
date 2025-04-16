package com.fu.mybatisplusdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 教程：https://blog.csdn.net/weixin_43933728/article/details/127462165
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.fu.mybatisplusdemo.mapper"}) //扫描mapper包，多个用英文逗号,隔开
public class MyBatisPlusDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBatisPlusDemoApplication.class, args);
    }

}

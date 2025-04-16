package com.fu.springbootreadwritesplittingdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 创建日期：2024-05-28
 */
@MapperScan("com.fu.springbootreadwritesplittingdemo.mapper")
@EnableTransactionManagement //开启事务管理
@SpringBootApplication
public class SpringBootReadwriteSplittingDemo {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReadwriteSplittingDemo.class, args);
    }

}
package com.fu.springbootdynamicdatasource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class SpringBootDynamicDataSourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDynamicDataSourceApplication.class, args);
    }

}
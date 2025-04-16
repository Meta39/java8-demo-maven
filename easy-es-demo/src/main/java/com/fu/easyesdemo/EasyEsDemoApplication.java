package com.fu.easyesdemo;

import cn.easyes.starter.register.EsMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EsMapperScan("com.fu.easyesdemo.mapper.ee") //还是那句话，建议和MyBatis-Plus的mapper分开存放
public class EasyEsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyEsDemoApplication.class, args);
    }

}

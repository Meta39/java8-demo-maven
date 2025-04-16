package com.fu.springbootjpademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing//审计，用于@Version、@CreatedDate、@CreatedBy、@LastModifiedDate、@LastModifiedBy注解自动填充字段值，实体类还需在类上使用 @EntityListeners(AuditingEntityListener.class) 注解
@SpringBootApplication
public class SpringBootJpaDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaDemoApplication.class,args);
    }
}

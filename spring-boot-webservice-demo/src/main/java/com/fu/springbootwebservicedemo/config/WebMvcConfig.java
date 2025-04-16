package com.fu.springbootwebservicedemo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        //引入 jackson-dataformat-xml 后，原本默认返回json变成了默认返回xml。因此这里要设置默认返回json
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

}
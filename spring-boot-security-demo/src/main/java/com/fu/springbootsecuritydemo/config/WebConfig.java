package com.fu.springbootsecuritydemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域问题
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //允许任何域名
                .allowedOriginPatterns("*")
                //允许任何方法
                .allowedMethods("*")
                //允许任何头
                .allowedHeaders("*")
                //暴露头
                .exposedHeaders("access-control-allow-headers",
                        "access-control-allow-methods",
                        "access-control-allow-origin",
                        "access-control-max-age",
                        "X-Frame-Options")
                // 是否允许证书（cookies）
                .allowCredentials(true)
                .maxAge(3600);
    }
}


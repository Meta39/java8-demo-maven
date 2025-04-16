package com.fu.miniodemo.minio;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {
    private String endpoint;//url
    private int port;//端口号
    private boolean secure;//https?true:false
    private String accessKey;//账号
    private String secretKey;//密码

    @Bean
    public MinioClient getMinioClient(){
        return MinioClient.builder().endpoint(endpoint, port, secure).credentials(accessKey, secretKey).build();
    }

}


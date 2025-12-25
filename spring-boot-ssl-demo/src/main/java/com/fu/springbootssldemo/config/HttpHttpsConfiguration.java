package com.fu.springbootssldemo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * http 配置
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class HttpHttpsConfiguration {
    @Value("${server.http-port:8080}")
    private int httpPort;
    private final ServerProperties serverProperties;

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();

        List<Connector> additionalConnectors = new ArrayList<>();
        
        // HTTP连接器配置
        Connector httpConnector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        httpConnector.setScheme("http");
        httpConnector.setPort(httpPort);
        httpConnector.setSecure(false);
        
        // 关键：不设置重定向，这样HTTP请求就不会自动转到HTTPS
        // httpConnector.setRedirectPort(8443); // 注释掉这行
        
        // 可选：自定义连接器参数
        httpConnector.setProperty("maxThreads", String.valueOf(serverProperties.getTomcat().getThreads().getMax()));

        additionalConnectors.add(httpConnector);

        // 可以添加更多连接器
        factory.addAdditionalTomcatConnectors(
                additionalConnectors.toArray(new Connector[]{})
        );

        // 可选：自定义Tomcat配置
        factory.addConnectorCustomizers( connector -> {
            // 可以在这里为特定连接器添加自定义配置
        });
        
        return factory;
    }
}
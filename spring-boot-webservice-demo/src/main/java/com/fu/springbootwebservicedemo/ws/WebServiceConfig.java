package com.fu.springbootwebservicedemo.ws;

import lombok.RequiredArgsConstructor;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * 打开地址为：ip:port/services/WebServiceEntry?wsdl
 * 建议用 soapUI 调用
 * 创建日期：2024-07-01
 */
@Configuration
@RequiredArgsConstructor
public class WebServiceConfig {
    private final WebServiceEntry webServiceEntry;
    private final WebServiceEntry12 webServiceEntry12;

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    /**
     * SOAP 1.1
     */
    @Bean
    public Endpoint endpoint11() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), webServiceEntry);
        endpoint.publish("/WebServiceEntry");
        return endpoint;
    }

    /**
     * SOAP 1.2
     */
    @Bean
    public Endpoint endpoint12() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), webServiceEntry12);
        endpoint.publish("/WebServiceEntry12");
        return endpoint;
    }

}

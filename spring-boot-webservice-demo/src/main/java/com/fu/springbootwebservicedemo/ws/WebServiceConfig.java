package com.fu.springbootwebservicedemo.ws;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * 打开地址为：ip:port/services/WebServiceEntry?wsdl
 * 建议用 soapUI 调用
 * 创建日期：2024-07-01
 */
@Configuration
public class WebServiceConfig {

    @Bean(name = "cxfServlet")
    public ServletRegistrationBean<?> cxfServlet() {
        //urlMappings默认是：services
        return new ServletRegistrationBean<>(new CXFServlet(), "/services/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    /**
     * SOAP 1.1
     */
    @Bean
    public Endpoint endpoint11() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), new WebServiceEntry());
        endpoint.publish("/WebServiceEntry");
        return endpoint;
    }

    /**
     * SOAP 1.2
     */
    @Bean
    public Endpoint endpoint12() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), new WebServiceEntry12());
        endpoint.publish("/WebServiceEntry12");
        return endpoint;
    }

}

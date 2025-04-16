package com.fu.springbootwebservicedemo.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

/**
 * SOAP 1.2
 */
@Slf4j
@Service
@WebService
//主要是下面这行，并且这行要配合 Spring 的 @Component、@Service 类似的注入注解才能使 SOAP 1.2 生效
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
public class WebServiceEntry12 {
    /*
        请求示例：
        POST：http://localhost:8080/services/WebServiceEntry12?wsdl
        入参：
        <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:tem="http://ws.springbootwebservicedemo.fu.com/">
           <soap:Header/>
           <soap:Body>
              <tem:hello>
                <params>
                    <![CDATA[<Params><Name>哈哈哈哈哈</Name></Params>]]>
                </params>
              </tem:hello>
           </soap:Body>
        </soap:Envelope>
        出参：
        <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">
            <soap:Body>
                <ns2:helloResponse xmlns:ns2="http://ws.springbootwebservicedemo.fu.com/">
                    <return>Hello World</return>
                </ns2:helloResponse>
            </soap:Body>
        </soap:Envelope>
     */
    @WebMethod
    public String hello(@WebParam(name = "params") String params) {
        log.info("params: {}", params);
        return "Hello World";
    }

}

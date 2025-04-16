package com.fu.springbootwebservicedemo.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fu.springbootwebservicedemo.util.ApplicationContextUtils;
import com.fu.springbootwebservicedemo.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

/**
 * SOAP 1.1(默认)
 * 创建日期：2024-07-01
 */
@Slf4j
@Service
@WebService
//主要是下面这行，并且这行要配合 Spring 的 @Component、@Service 类似的注入注解才能使 SOAP 1.1 生效（当前Apache CXF 默认是 SOAP 1.1）
@BindingType(value = SOAPBinding.SOAP11HTTP_BINDING)
public class WebServiceEntry {

    /**
     * 通过实现了 IWebService 接口的 bean name 反射调用 handle 方法
     *
     * @param service   bean name
     * @param parameter XML 字符串请求参数
     */
    @WebMethod
    @SuppressWarnings("unchecked")
    public <T> String invoke(@WebParam(name = "service") String service, @WebParam(name = "parameter") String parameter) throws JsonProcessingException {
        IWebService<T> webService = (IWebService<T>) ApplicationContextUtils.getBean(service);

        // 通过缓存获取 IWebService 实现类的 handle 函数泛型类型入参，这样就不用每次请求都通过反射去获取入参，提升了程序性能。
        Class<T> parameterType = (Class<T>) WebServiceTypeCache.getParameterType(service);
        // 使用 Jackson-XML 将 XML 字符串转换为 Java 对象
        T reqObject = JacksonUtils.XML.readValue(parameter, parameterType);

        R<?> r;
        try {
            r = R.ok(webService.handle(reqObject));
        } catch (Exception e) {
            String message = e.getMessage();
            log.error(message, e);
            r = R.err(message);
        }
        return JacksonUtils.XML.writeValueAsString(r);
    }

}

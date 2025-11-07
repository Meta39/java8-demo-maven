package com.fu.springbootwebservicedemo.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fu.springbootwebservicedemo.config.App;
import com.fu.springbootwebservicedemo.util.BeanUtils;
import com.fu.springbootwebservicedemo.util.JacksonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;
import java.util.Set;

/**
 * SOAP 1.1(默认)
 * 创建日期：2024-07-01
 */
@Slf4j
@Service
@WebService(targetNamespace = "http://ws.springbootwebservicedemo.fu.com/")
//主要是下面这行，并且这行要配合 Spring 的 @Component、@Service 类似的注入注解才能使 SOAP 1.1 生效（当前Apache CXF 默认是 SOAP 1.1）
@BindingType(value = SOAPBinding.SOAP11HTTP_BINDING)
@RequiredArgsConstructor
public class WebServiceEntry {
    private final App app;
    private final Validator validator;

    /**
     * 通过实现了 IWebService 接口的 bean name 反射调用 handle 方法
     *
     * @param service   bean name
     * @param parameter XML 字符串请求参数
     */
    /*
        请求示例：
        POST：http://localhost:95/services/WebServiceEntry?wsdl
        入参：
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://ws.springbootwebservicedemo.fu.com/">
            <soapenv:Header/>
            <soapenv:Body>
                <ws:invoke>
                    <service>Hello</service>
                    <parameter>
                        <![CDATA[
                    <Params><Name>哈哈</Name><Works><Work><WorkName>Java</WorkName></Work>
                        </Works>
                    </Params>
                ]]>
            </parameter>
        </ws:invoke>
        </soapenv:Body>
        </soapenv:Envelope>
     */
    @WebMethod
    @SuppressWarnings("unchecked")
    public <T> String invoke(@WebParam(name = "service") String service, @WebParam(name = "parameter") String parameter) throws JsonProcessingException {
        try {
            //校验 bean 是否存在。
            validateImpl(service);

            // 缓存获取具体实现类的参数类型，再把字符串通过jackson转为具体的参数类型。
            T reqObject = JacksonUtils.XML.readValue(parameter, (Class<T>) WebServiceTypeCache.getParameterType(service));

            //参数校验
            validateParameter(reqObject);

            //执行方法
            return JacksonUtils.XML.writeValueAsString(R.ok(((IWebService<T>) BeanUtils.getBean(service)).handle(reqObject)));
        } catch (Exception e) {
            String message = e.getMessage();
            log.error("invoke {} fail:{}\nparameter:{}", service, message, parameter, e);
            return JacksonUtils.XML.writeValueAsString(R.err(message));
        }
    }

    //接口实现校验
    private void validateImpl(String service) {
        if (!WebServiceTypeCache.hasServiceImpl(service)) {
            throw new IllegalArgumentException(service + " not found");
        }
    }

    //参数校验
    private <T> void validateParameter(T reqObject) {
        if (reqObject == null) return;
        if (reqObject instanceof String && !StringUtils.hasText((String) reqObject)) return;
        //判断是否开启参数校验，默认开启。
        if (app.isValidate()) {
            Set<ConstraintViolation<T>> violations = validator.validate(reqObject);
            if (!CollectionUtils.isEmpty(violations)) {
                ConstraintViolation<T> violation = violations.iterator().next();
                //有一个参数校验失败就立即抛出异常，而不是全部校验完才抛出，这样可以提高性能并提供更及时的反馈。
                throw new IllegalArgumentException(violation.getPropertyPath() + " " + violation.getMessage());
            }
        }
    }

}

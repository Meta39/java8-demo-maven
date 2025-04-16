package com.fu.springbootwebservicedemo.ws.service;

import com.fu.springbootwebservicedemo.dto.HelloReq;
import com.fu.springbootwebservicedemo.dto.HelloRes;
import com.fu.springbootwebservicedemo.dto.Work;
import com.fu.springbootwebservicedemo.ws.IWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 实现 IWebService 接口，统一调用
 * 创建日期：2024-07-02
 */
@Slf4j
@Service("Hello")
public class HelloWebServiceImpl implements IWebService<HelloReq> {

    /*
        请求示例：
        POST：http://localhost:8080/services/WebServiceEntry?wsdl
        入参：
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://ws.springbootwebservicedemo.fu.com/">
        <soapenv:Header/>
        <soapenv:Body>
        <ws:invoke>
        <service>Hello</service>
        <parameter><![CDATA[
            <Params>
                <Name>哈哈</Name>
                <Works>
                    <Work>
                        <WorkName>Java</WorkName>
                    </Work>
                </Works>
            </Params>
        ]]></parameter>
              </ws:invoke>
           </soapenv:Body>
        </soapenv:Envelope>

        出参：
        <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
                <ns2:invokeResponse xmlns:ns2="http://ws.springbootwebservicedemo.fu.com/">
                    <return>&lt;R&gt;&lt;Code&gt;200&lt;/Code&gt;&lt;Message&gt;success&lt;/Message&gt;&lt;Data&gt;&lt;Name&gt;哈哈&lt;/Name&gt;&lt;Age&gt;18&lt;/Age&gt;&lt;/Data&gt;&lt;/R&gt;</return>
                </ns2:invokeResponse>
            </soap:Body>
        </soap:Envelope>

        美化出参：
        <R><Code>200</Code><Message>success</Message><Data><Name>哈哈</Name><Age>18</Age></Data></R>
     */
    @Override
    public HelloRes handle(HelloReq req) {
        String name = req.getName();
        List<Work> works = req.getWorks();

        if (!StringUtils.hasText(name)) {
            throw new RuntimeException("Name 不能为空");
        }
        if (!CollectionUtils.isEmpty(works)) {
            for (Work work : works) {
                String workName = work.getWorkName();
                log.info("workName={}", workName);
            }
        }

        HelloRes res = new HelloRes();
        res.setName(name);
        res.setAge(18);
        return res;
    }

}

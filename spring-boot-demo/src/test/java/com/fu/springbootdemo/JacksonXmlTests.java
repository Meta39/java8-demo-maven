package com.fu.springbootdemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fu.springbootdemo.entity.XmlObeject;
import com.fu.springbootdemo.entity.XmlObejectInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JacksonXmlTests {

    @Test
    void test() throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();

        XmlObeject xmlObeject = new XmlObeject();
        xmlObeject.setName("XML");

        //集合
        List<XmlObejectInfo> xmlObejectInfos = new ArrayList<>();

        XmlObejectInfo xmlObejectInfo = new XmlObejectInfo();
        xmlObejectInfo.setAge(25);

        xmlObejectInfos.add(xmlObejectInfo);

        xmlObeject.setXmlObejectInfos(xmlObejectInfos);

        //序列化成XML字符串
        String xmlString = xmlMapper.writeValueAsString(xmlObeject);
        log.info("{}", xmlString);
        //反序列化成Java对象
        XmlObeject xmlObj = xmlMapper.readValue(xmlString, XmlObeject.class);
        log.info("{}", xmlObj);
    }

}

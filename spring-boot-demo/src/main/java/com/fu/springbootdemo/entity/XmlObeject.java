package com.fu.springbootdemo.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JacksonXmlRootElement(localName = "Xml")//自定义根路径名称
public class XmlObeject implements Serializable {
    @JacksonXmlProperty(localName = "Name")
    private String name;
    /**
     * JacksonXmlElementWrapper注解用于指定列表或数组属性在XML中的包装元素，以提供更好的结构化层次和语义意义。
     * 简单理解就是外层用@JacksonXmlElementWrapper，内层用@JacksonXmlProperty。
     */
    @JacksonXmlElementWrapper(localName = "XmlObejectInfos")//序列化
    @JacksonXmlProperty(localName = "XmlInfo")//序列化和反序列化优先级比@JacksonXmlElementWrapper注解高
    private List<XmlObejectInfo> xmlObejectInfos;
}

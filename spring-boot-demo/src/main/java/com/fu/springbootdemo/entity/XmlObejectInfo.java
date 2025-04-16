package com.fu.springbootdemo.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.io.Serializable;

@Data
@JacksonXmlRootElement(localName = "XmlInfo")
public class XmlObejectInfo implements Serializable {
    @JacksonXmlProperty(localName = "Age")
    private Integer age;
}

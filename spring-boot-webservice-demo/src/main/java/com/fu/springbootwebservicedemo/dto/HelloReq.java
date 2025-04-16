package com.fu.springbootwebservicedemo.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "Params")
public class HelloReq {

    @JacksonXmlProperty(localName = "Name")
    private String name;

    @JacksonXmlElementWrapper(localName = "Works")
    @JacksonXmlProperty(localName = "Work")
    private List<Work> works;

}
package com.fu.springbootwebservicedemo.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "Work")
public class Work {

    @JacksonXmlProperty(localName = "WorkName")
    private String workName;

}

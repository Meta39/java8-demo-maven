package com.fu.springbootdynamicservicedemo.dynamicservice;

import com.fu.springbootdynamicservicedemo.annotation.DynamicMethod;
import com.fu.springbootdynamicservicedemo.annotation.DynamicService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DynamicService
public class TestDynamicService {

    @DynamicMethod
    public void noParamMethod() {
        log.info("noParamMethod");
    }

}

package com.fu.springbootdynamicservicedemo.dynamicservice;

import com.fu.springbootdynamicservicedemo.annotation.DynamicMethod;
import com.fu.springbootdynamicservicedemo.annotation.DynamicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@DynamicService
public class TestDynamicService {

    @DynamicMethod
    @Async
    public void noParamMethod() {
        log.info("noParamMethod");
    }

}

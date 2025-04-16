package com.fu.springbootdemo.async;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 异步调用
 */
@Slf4j
@Component
public class AsyncThread {

    @SneakyThrows
    @Async //异步线程注解
    public void asyncThread(){
        log.info("异步线程开始执行...");
        Thread.sleep(3000);
        log.info("异步线程执行结束...");
    }
}

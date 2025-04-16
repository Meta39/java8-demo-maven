package com.fu.springbootdemo.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 主线程
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MainThread {
    private final AsyncThread asyncThread;

    public String mainThread(){
        log.info("主线程开始执行...");
        asyncThread.asyncThread();
        log.info("主线程执行结束...");
        return "ok";
    }

}

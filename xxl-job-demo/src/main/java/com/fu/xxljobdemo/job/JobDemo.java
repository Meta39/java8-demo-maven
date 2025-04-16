package com.fu.xxljobdemo.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component //交给spring管理
public class JobDemo {

    /**
     * xxl-job定时任务注解
     */
    @XxlJob("jobDemo")
    public void jobDemo(){
        log.info("这是一个由Xxl-job调度的定时任务");
    }

}

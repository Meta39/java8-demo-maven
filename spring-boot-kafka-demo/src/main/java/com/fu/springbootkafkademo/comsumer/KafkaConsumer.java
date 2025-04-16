package com.fu.springbootkafkademo.comsumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Kafka消费者
 */
@Slf4j
@Configuration
public class KafkaConsumer {

    /**
     * 监听topics主题
     * @param msg 对应主题的生产者传过来的消息内容
     */
    @KafkaListener(topics = "first")
    public void first(String msg){
        log.info("get message: {}",msg);
    }

}

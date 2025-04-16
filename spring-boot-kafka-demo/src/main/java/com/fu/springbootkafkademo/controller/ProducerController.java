package com.fu.springbootkafkademo.controller;

import com.fu.springbootkafkademo.enums.Topics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor //lombok 如果变量为private final则自动注入
public class ProducerController {
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 往kafka topic为first发送消息msg
     * @param msg 消息
     */
    @GetMapping("send")
    public String hello(String msg) {
        //通过kafka发送消息
        kafkaTemplate.send(Topics.first.name(), msg);
        return "send message：" + msg;
    }

}
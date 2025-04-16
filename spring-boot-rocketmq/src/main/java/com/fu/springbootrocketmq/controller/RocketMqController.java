package com.fu.springbootrocketmq.controller;

import com.fu.springbootrocketmq.producer.MqProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建日期：2024-05-11
 */
@RestController
@RequiredArgsConstructor
public class RocketMqController {
    private final MqProducer mqProducer;

    /**
     * 发送消息
     * @param topic 消息主题
     * @param message 消息内容
     */
    @GetMapping("send")
    public void send(@RequestParam(name = "topic", required = false) String topic, @RequestParam(name = "message") String message) {
        if (!StringUtils.hasText(topic)){
            topic = "my-topic";
        }
        mqProducer.sendMessage(topic, message);
    }

}

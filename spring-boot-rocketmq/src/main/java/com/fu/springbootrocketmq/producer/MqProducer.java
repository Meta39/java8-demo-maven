package com.fu.springbootrocketmq.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

/**
 * 消息生产者（一般情况，生产者和消费者不在同一个项目）
 * 创建日期：2024-05-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MqProducer {
    private final RocketMQTemplate rocketMQTemplate;

    /**
     * 发送消息
     * @param topic 消息主题
     * @param message 消息内容
     */
    public void sendMessage(String topic, String message) {
        rocketMQTemplate.convertAndSend(topic, message);
    }

}

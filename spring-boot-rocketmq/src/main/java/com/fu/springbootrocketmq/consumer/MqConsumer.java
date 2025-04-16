package com.fu.springbootrocketmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * 消息消费者（一般情况，生产者和消费者不在同一个项目）
 * 创建日期：2024-05-11
 */
@Slf4j
@Service
@RocketMQMessageListener(topic = "my-topic", consumerGroup = "my-consumer_group")
public class MqConsumer implements RocketMQListener<String> {

    /**
     * 消费对应 topic 主题的消息
     * @param message 生产者发送过来的消息
     */
    @Override
    public void onMessage(String message) {
        // 处理接收到的消息
        log.info("接收到topic = my-topic的消息，内容为: {}", message);
    }

}
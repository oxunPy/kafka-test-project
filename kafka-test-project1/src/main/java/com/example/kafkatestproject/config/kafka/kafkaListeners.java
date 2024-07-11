package com.example.kafkatestproject.config.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class kafkaListeners {
    @KafkaListener(topics = {"${kafka.topic.reply.product}"}, groupId = "${kafka.consumer.reply.group_id}", containerFactory = "replyKafkaListenerContainerFactory")
    public void productReplyListener(String data) {
        System.out.println(data);
    }
}

package com.example.kafkatestproject.config.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private static int count = 0;

    @KafkaListener(topics = {"${kafka.topic.request.unit}"}, groupId = "${kafka.consumer.group_id}", containerFactory = "replyKafkaListenerContainerFactory")
    public void unitListener(String data) {
        System.out.println(data);
        count++;

        if(count == 1000_000)
            System.out.println("Success!!!");
    }

    @KafkaListener(topics = {"${kafka.topic.request.product}"}, groupId = "${kafka.consumer.group_id}", containerFactory = "replyKafkaListenerContainerFactory")
    @SendTo("${kafka.topic.reply.product}")
    public String productListener(String data) {
        System.out.println(data);
        return "Replying product -> KAFKA-TEST-PROJECT(2)";
    }
}

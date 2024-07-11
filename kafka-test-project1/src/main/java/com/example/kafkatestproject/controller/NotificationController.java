package com.example.kafkatestproject.controller;

import com.example.kafkatestproject.sender.NotificationSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private final NotificationSender notificationSender;

    @Value("${kafka.topic.request.unit}")
    private String unitTopic;

    @Value("${kafka.topic.request.product}")
    private String productTopic;

    @Value("${kafka.topic.reply.product}")
    private String productReplyTopic;


    public NotificationController(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    @PostMapping("/send/topic")
    public void sendTopicMessage() {
        for (int i = 0; i < 1000000; i++)
            notificationSender.send(unitTopic, "Sending unit -> KAFKA-TEST-PROJECT(1)");
    }


    @PostMapping("/send-and-reply/topic")
    public void sendAndReplyTopicMessage() {
        notificationSender.sendWithReply(productTopic, productReplyTopic, "Sending product -> KAFKA-TEST-PROJECT(1)");
    }
}

package com.example.kafkatestproject.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {


    @Value("${kafka.topic.request.unit}")
    private String requestUnit;
    @Value("${kafka.topic.request.product}")
    private String requestProduct;

    @Value("${kafka.topic.reply.unit}")
    private String replyUnit;
    @Value("${kafka.topic.reply.product}")
    private String replyProduct;

    @Bean
    public KafkaAdmin.NewTopics requests() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(requestUnit)
                        .build(),
                TopicBuilder.name(requestProduct)
                        .build());
    }

    @Bean
    public NewTopic replyUnit() {
        return TopicBuilder.name(replyUnit)
                .build();
    }

    @Bean
    public KafkaAdmin.NewTopics replies() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(replyProduct)
                        .build());
    }
}
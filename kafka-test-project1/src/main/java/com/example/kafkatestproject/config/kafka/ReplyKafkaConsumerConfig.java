package com.example.kafkatestproject.config.kafka;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;

import java.util.Map;

@Configuration
public class ReplyKafkaConsumerConfig {

    @Value("${kafka.consumer.reply.group_id}")
    private String replyGroupId;

    @Value("${kafka.topic.reply.product}")
    private String replyProductTopic;

    @Value("${kafka.topic.reply.unit}")
    private String replyUnitTopic;

    @Bean
    public ProducerFactory<String, String> replyProducerFactory(@Qualifier("producerConfig") Map producerConfig) {
        return new DefaultKafkaProducerFactory<>(producerConfig);
    }

    @Bean
    public KafkaTemplate<String, String> replyTemplate(@Qualifier("replyProducerFactory") ProducerFactory<String, String> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> replyKafkaListenerContainerFactory(@Qualifier("consumerFactory") ConsumerFactory<String, String> cf,
                                                                                                                                KafkaTemplate<String, String> replyTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        factory.setReplyTemplate(replyTemplate);
        return factory;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> repliesContainer(@Qualifier("consumerFactory") ConsumerFactory<String, String> consumerFactory) {
        ContainerProperties containerProperties = new ContainerProperties(replyUnitTopic, replyProductTopic);
        containerProperties.setGroupId(replyGroupId);
        // Set the message listener
        containerProperties.setMessageListener((MessageListener<String, String>) record -> {
            // Process the reply message
            System.out.println("Received reply: " + record.value());
        });
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
    }
}
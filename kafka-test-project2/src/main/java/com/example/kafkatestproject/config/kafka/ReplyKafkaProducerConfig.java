
package com.example.kafkatestproject.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.Map;

@Configuration
public class ReplyKafkaProducerConfig {

    @Value("${kafka.consumer.group_id}")
    private String groupId;
    @Value("${kafka.topic.reply.unit}")
    private String replyUnit;
    @Value("${kafka.topic.reply.product}")
    private String replyProduct;

    @Bean
    public ConsumerFactory<String, String> replyConsumerFactory(@Qualifier("consumerConfig") Map consumerConfig) {
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return new DefaultKafkaConsumerFactory<>(consumerConfig);
    }

//    @Bean
//    public KafkaMessageListenerContainer<String, String> replyContainer(@Qualifier("replyConsumerFactory") ConsumerFactory<String, String> cf) {
//        ContainerProperties containerProperties = new ContainerProperties(replyUnit, replyProduct);
//        return new KafkaMessageListenerContainer<>(cf, containerProperties);
//    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyKafkaTemplate(@Qualifier("producerFactory") ProducerFactory<String, String> pf,
                                                                            @Qualifier("repliesContainer") ConcurrentMessageListenerContainer<String, String> replyContainer) {
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }
}
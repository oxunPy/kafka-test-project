package com.example.kafkatestproject.sender;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.Duration;

@Component
public class NotificationSender {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

    public NotificationSender(KafkaTemplate<String, String> kafkaTemplate, ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    public void send(String topic, String dataJson) {
        /* send to broker */
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, dataJson);
        future.completable().whenCompleteAsync((result, ex) -> {
            if (ex == null) {
                System.out.println("Success[1]: " + result);
            } else {
                System.out.println("Error[1]: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    public void sendWithReply(String requestTopic, String replyTopic, String dataJson) {

        ProducerRecord<String, String> record = new ProducerRecord<>(requestTopic, dataJson);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));

        RequestReplyFuture<String, String, String> replyFuture = replyingKafkaTemplate.sendAndReceive(record, Duration.ofSeconds(30));

        /* send to broker */
        ListenableFuture<SendResult<String, String>> future = replyFuture.getSendFuture();
        future.completable().whenCompleteAsync((result, ex) -> {
            if(ex == null) {
                System.out.println("Success[2]: " + result.toString());

            } else {
                System.out.println("Error[2]: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        /* reply from after listen */
        replyFuture.completable().whenCompleteAsync((consumerRecord, ex) -> {
            if(ex == null) {
                System.out.println("Success[2]:" + consumerRecord.value());
            } else {
                System.out.println("Error[2]: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
}

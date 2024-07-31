package com.ecommerce.app.config;

import com.ecommerce.app.enums.KafkaTopicName;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

    @Bean
    public NewTopic createOrderTopic() {
        return TopicBuilder
                .name(KafkaTopicName.ORDER)
                .build();
    }

    @Bean
    public NewTopic createRollbackPurchaseTopic() {
        return TopicBuilder
                .name(KafkaTopicName.ROLLBACK_PURCHASE)
                .build();
    }
}

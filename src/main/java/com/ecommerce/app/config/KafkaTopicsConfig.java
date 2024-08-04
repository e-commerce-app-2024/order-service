package com.ecommerce.app.config;

import com.ecommerce.app.enums.KafkaTopicName;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class KafkaTopicsConfig {

    final List<String> topicList = Arrays.asList(KafkaTopicName.ORDER, KafkaTopicName.ROLLBACK_PURCHASE, KafkaTopicName.DELETE_PURCHASE);

    @Bean
    public List<NewTopic> createTopicList() {
        List<NewTopic> newTopics = new ArrayList<>();
        topicList.forEach(topic -> {
            NewTopic newTopic = TopicBuilder
                    .name(topic)
                    .partitions(1)
                    .replicas(1)
                    .build();
            newTopics.add(newTopic);
        });
        return newTopics;
    }
}

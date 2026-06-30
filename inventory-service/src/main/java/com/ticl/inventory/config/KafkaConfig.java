package com.ticl.inventory.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${app.kafka.inventory-topic}")
    private String INVENTORY_TOPIC;

    // Registering Kafka Topic with 3 partitions and 1 replica
    @Bean
    public NewTopic kafkaTopic(){
        return TopicBuilder
                .name(INVENTORY_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}

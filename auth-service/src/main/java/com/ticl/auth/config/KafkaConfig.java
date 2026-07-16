package com.ticl.auth.config;

import com.ticl.commons.constant.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    // Registering Kafka Topic with 3 partitions and 1 replica
    @Bean
    public NewTopic UserLogin(){
        return TopicBuilder
                .name(KafkaTopics.USER_LOGIN)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic UserLogout(){
        return TopicBuilder
                .name(KafkaTopics.USER_LOGOUT)
                .partitions(3)
                .replicas(1)
                .build();
    }
}

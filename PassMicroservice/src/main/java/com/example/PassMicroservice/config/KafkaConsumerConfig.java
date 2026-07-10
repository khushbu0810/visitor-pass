package com.example.PassMicroservice.config;

import com.example.core.events.PassCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

//building consumer factory from application.properties
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PassCreatedEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, PassCreatedEvent> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, PassCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}
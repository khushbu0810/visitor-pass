package com.example.VisitorMicroservice.producer;

import com.example.core.events.PassCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PassProducer {
    private final KafkaTemplate<String, PassCreatedEvent>kafkaTemplate;

    public PassProducer(KafkaTemplate<String, PassCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishVisitorCreated(PassCreatedEvent event){
        kafkaTemplate.send("pass-created",event);
        log.info("PassCreatedEvent Published : {}", event.getVisitorId());
    }
}

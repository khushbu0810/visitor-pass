package com.example.PassMicroservice.consumer;

import com.example.PassMicroservice.service.PassService;
import com.example.core.events.PassCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PassConsumer {
    private final PassService passService;

    public PassConsumer(PassService passService) {
        this.passService = passService;
    }

    @KafkaListener(topics = "pass-created",groupId = "pass-group")
    public void consumePassEvent(PassCreatedEvent event){
        log.info("PassCreatedEvent received: {}", event.getVisitorId());
        passService.generatePassForVisitor(event.getResidentId(),event.getVisitorId());
        log.info("QR pass Generated");
    }
}

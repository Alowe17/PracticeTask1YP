package com.auth.producer;

import com.auth.model.dto.AuditEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;

    public void sendAuditEvent (AuditEvent dto) {
        String topic = "audit-service";
        log.info("Отправка события. Тип: {}, uuid: {}! Дата: {}", dto.getType(), dto.getUuid(), OffsetDateTime.now());
        kafkaTemplate.send(topic, dto);
    }
}
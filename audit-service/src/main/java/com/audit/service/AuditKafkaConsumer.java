package com.audit.service;

import com.audit.model.dto.AuditEventMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditKafkaConsumer {
    private final AuditEventService auditEventService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "audit-service", groupId = "audit-service-group")
    public void consume (String payload) throws JsonProcessingException {
        try {
            AuditEventMessage auditEventMessage = objectMapper.readValue(payload, AuditEventMessage.class);
            log.info("Получено событие аудита. Тип: {}, uuid: {}, дата: {}", auditEventMessage.getType(), auditEventMessage.getUuid(), auditEventMessage.getCreateTime());
            auditEventService.saveAuditEvent(auditEventMessage);
        } catch (JsonProcessingException exception) {
            log.error("Не удалось преобразовать Kafka-сообщение в событие аудита. Payload: {}", payload, exception);
            throw exception;
        } catch (RuntimeException exception) {
            log.error("Не удалось сохранить Kafka-событие аудита. Payload: {}", payload, exception);
            throw exception;
        }
    }
}

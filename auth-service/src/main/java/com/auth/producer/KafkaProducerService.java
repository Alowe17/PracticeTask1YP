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
        kafkaTemplate.send(topic, dto).whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Не удалось отправить событие аудита. Тип: {}, uuid: {}", dto.getType(), dto.getUuid(), exception);
                return;
            }

            log.info("Событие аудита отправлено. Topic: {}, partition: {}, offset: {}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
        });
    }
}

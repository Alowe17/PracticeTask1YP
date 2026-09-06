package com.auth.producer;

import com.auth.model.dto.RegisterUserLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, RegisterUserLog> kafkaTemplate;

    public void sendLogRegister (RegisterUserLog dto) {
        String topic = "audit-service";
        log.info("Отправлены новые данный для логирования! {}", OffsetDateTime.now());
        kafkaTemplate.send(topic, dto);
    }
}
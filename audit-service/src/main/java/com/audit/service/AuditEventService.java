package com.audit.service;

import com.audit.model.dto.AuditEventMessage;
import com.audit.model.dto.AuditEventRs;
import com.audit.model.entity.AuditEvent;
import com.audit.repository.AuditEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditEventService {
    private final AuditEventRepository auditEventRepository;

    @Transactional
    public void saveAuditEvent (AuditEventMessage dto) {
        AuditEvent auditEvent = AuditEvent.builder()
                .userId(dto.getUuid())
                .message(dto.getMessage())
                .createdAt(dto.getCreateTime())
                .auditEventType(dto.getType())
                .build();

        AuditEvent savedEvent = auditEventRepository.save(auditEvent);
        log.info("Событие аудита сохранено в БД. uuid: {}, userId: {}, тип: {}",
                savedEvent.getUuid(), savedEvent.getUserId(), savedEvent.getAuditEventType());
    }

    public List<AuditEventRs> findAll () {
        List<AuditEvent> auditEvents = auditEventRepository.findAll();
        List<AuditEventRs> auditEventRsList = new ArrayList<>();

        for (AuditEvent auditEvent : auditEvents) {
            AuditEventRs auditEventRs = AuditEventRs.builder()
                    .uuid(auditEvent.getUuid())
                    .userId(auditEvent.getUserId())
                    .message(auditEvent.getMessage())
                    .createdAt(auditEvent.getCreatedAt())
                    .type(auditEvent.getAuditEventType())
                    .build();

            auditEventRsList.add(auditEventRs);
        }

        return auditEventRsList;
    }

    public List<AuditEventRs> findByUserId (String userId) {
        List<AuditEvent> auditEvents = auditEventRepository.findByUserId(userId);
        List<AuditEventRs> auditEventRsList = new ArrayList<>();

        for (AuditEvent auditEvent : auditEvents) {
            AuditEventRs auditEventRs = AuditEventRs.builder()
                    .uuid(auditEvent.getUuid())
                    .userId(auditEvent.getUserId())
                    .message(auditEvent.getMessage())
                    .createdAt(auditEvent.getCreatedAt())
                    .type(auditEvent.getAuditEventType())
                    .build();

            auditEventRsList.add(auditEventRs);
        }

        return auditEventRsList;
    }
}

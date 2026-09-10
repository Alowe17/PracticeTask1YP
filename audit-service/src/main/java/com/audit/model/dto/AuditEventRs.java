package com.audit.model.dto;

import com.audit.model.entity.AuditEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuditEventRs {
    private String uuid;
    private String userId;
    private String message;
    private OffsetDateTime createdAt;
    private AuditEventType type;
}
package com.audit.model.dto;

import com.audit.model.entity.AuditEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditEventMessage {
    private String uuid;
    private String message;
    private OffsetDateTime createTime;
    private AuditEventType type;
}
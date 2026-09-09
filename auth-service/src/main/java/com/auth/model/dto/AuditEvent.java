package com.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditEvent {
    private String uuid;
    private String message;
    private OffsetDateTime createTime;
    private AuditEventType type;
}
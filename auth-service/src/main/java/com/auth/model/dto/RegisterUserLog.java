package com.auth.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class RegisterUserLog {
    private String uuid;
    private String message;
    private OffsetDateTime createTime = OffsetDateTime.now();
    private TypeLog type;
}
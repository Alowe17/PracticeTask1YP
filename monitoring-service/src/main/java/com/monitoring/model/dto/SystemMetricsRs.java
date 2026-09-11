package com.monitoring.model.dto;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SystemMetricsRs {
    private double cpu;
    private Long memoryTotal;
    private Long memoryUsed;
    private Long memoryFree;
    private Long upTime;
    private Long processors;
    private String javaVersion;
    private String operatingSystem;
    @CreatedDate
    private OffsetDateTime createdAt;
}
package com.monitoring.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Entity
@Table(name = "system_metrics")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
public class SystemMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;
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
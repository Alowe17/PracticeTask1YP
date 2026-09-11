package com.monitoring.repository;

import com.monitoring.model.entity.SystemMetrics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemMetricsRepository extends JpaRepository<SystemMetrics, String> {
    Optional<SystemMetrics> findTopByOrderByCreatedAtDesc();
}
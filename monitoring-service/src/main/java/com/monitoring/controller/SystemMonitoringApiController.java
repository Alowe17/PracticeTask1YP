package com.monitoring.controller;

import com.monitoring.model.dto.SystemMetricsRs;
import com.monitoring.service.SystemMetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/monitoring/api")
@RequiredArgsConstructor
@Tag(
        name = "System Monitoring",
        description = "Получение информации о состоянии системы"
)
public class SystemMonitoringApiController {

    private final SystemMetricsService systemMetricsService;

    @GetMapping("/current")
    @Operation(summary = "Получить текущие метрики системы")
    public SystemMetricsRs getCurrent() {
        return systemMetricsService.getCurrent();
    }

    @GetMapping("/history")
    @Operation(summary = "Получить историю метрик")
    public List<SystemMetricsRs> getHistory() {
        return systemMetricsService.getHistory();
    }

    @PostMapping("/collect")
    @Operation(summary = "Принудительно собрать снимок метрик")
    public SystemMetricsRs collect() {
        return systemMetricsService.collect();
    }
}
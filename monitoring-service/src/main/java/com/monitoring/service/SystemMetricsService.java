package com.monitoring.service;

import com.monitoring.model.dto.SystemMetricsRs;
import com.monitoring.model.entity.SystemMetrics;
import com.monitoring.repository.SystemMetricsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SystemMetricsService {
    private final SystemMetricsRepository systemMetricsRepository;

    @Scheduled(fixedRate = 60000)
    public SystemMetricsRs collect() {
        log.info("Начат сбор данных!");
        try {
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

            double cpuUsage = getCpuUsage();
            long memoryTotal = memoryMXBean.getHeapMemoryUsage().getMax();
            long memoryUsed = memoryMXBean.getHeapMemoryUsage().getUsed();
            long memoryFree = memoryTotal - memoryUsed;
            long uptime = runtimeMXBean.getUptime();
            long processors = Runtime.getRuntime().availableProcessors();

            SystemMetrics metrics = SystemMetrics.builder()
                    .cpu(cpuUsage)
                    .memoryTotal(memoryTotal)
                    .memoryUsed(memoryUsed)
                    .memoryFree(memoryFree)
                    .upTime(uptime)
                    .processors(processors)
                    .javaVersion(System.getProperty("java.version"))
                    .operatingSystem(System.getProperty("os.name"))
                    .createdAt(OffsetDateTime.now())
                    .build();

            SystemMetrics saved = systemMetricsRepository.save(metrics);
            log.info("Снимок системных метрик сохранён. id={}", saved.getUuid());
            return mapToDto(saved);

        } catch (Exception e) {

            log.error("Ошибка при сборе системных метрик: {}", e.getMessage());

            throw new RuntimeException("Не удалось собрать системные метрики", e);
        }
    }

    private double getCpuUsage() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();

        if (operatingSystemMXBean instanceof com.sun.management.OperatingSystemMXBean osBean) {
            return osBean.getCpuLoad() * 100;
        }

        return -1;
    }

    public SystemMetricsRs getCurrent() {
        return systemMetricsRepository
                .findTopByOrderByCreatedAtDesc()
                .map(this::mapToDto)
                .orElseGet(() -> this.mapToDto(this.collect()));
    }

    private SystemMetricsRs mapToDto(SystemMetrics metric) {
        if (metric == null) {
            return null;
        }

        return SystemMetricsRs.builder()
                .cpu(metric.getCpu())
                .memoryTotal(metric.getMemoryTotal())
                .memoryUsed(metric.getMemoryUsed())
                .memoryFree(metric.getMemoryFree())
                .upTime(metric.getUpTime())
                .processors(metric.getProcessors())
                .javaVersion(metric.getJavaVersion())
                .operatingSystem(metric.getOperatingSystem())
                .createdAt(metric.getCreatedAt())
                .build();
    }

    public List<SystemMetricsRs> getHistory() {
        return systemMetricsRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this::mapToDto)
                .toList();
    }
}
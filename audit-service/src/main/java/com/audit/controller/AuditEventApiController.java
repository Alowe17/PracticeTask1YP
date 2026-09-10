package com.audit.controller;

import com.audit.model.dto.AuditEventRs;
import com.audit.service.AuditEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit/api")
@RequiredArgsConstructor
public class AuditEventApiController {
    private final AuditEventService auditEventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AuditEventRs> getAllAuditEvents() {
        return auditEventService.findAll();
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<AuditEventRs> getAuditEventsByUserId(@PathVariable String userId) {
        return auditEventService.findByUserId(userId);
    }
}
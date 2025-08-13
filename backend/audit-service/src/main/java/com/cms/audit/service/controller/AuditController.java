package com.cms.audit.service.controller;

import com.cms.audit.service.model.AuditEvent;
import com.cms.audit.service.model.AuditRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditRepository repository;

    @PostMapping
    public AuditEvent append(@RequestBody @Valid AuditEvent event) {
        if (event.getTimestamp() == null) event.setTimestamp(Instant.now());
        return repository.save(event);
    }

    @GetMapping
    public List<AuditEvent> list() { return repository.findAll(); }
}

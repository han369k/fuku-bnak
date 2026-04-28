package com.javaeasybank.risk.controller;

import com.javaeasybank.risk.dto.RiskEventResponse;
import com.javaeasybank.risk.service.RiskEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/riskevent")
@RequiredArgsConstructor
public class RiskEventController {
    private final RiskEventService riskEventService;

    @GetMapping
    public ResponseEntity<List<RiskEventResponse>> getAll() {

        List<RiskEventResponse> riskLogs = riskEventService.findAll();

        return ResponseEntity.ok(riskLogs);
    }
}

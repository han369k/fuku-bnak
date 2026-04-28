package com.javaeasybank.risk.service;

import com.javaeasybank.risk.dto.RiskEventResponse;
import com.javaeasybank.risk.entity.RiskEventLog;
import com.javaeasybank.risk.repository.RiskEventLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiskEventService {

    private final RiskEventLogRepository riskEventLogRepository;

    public List<RiskEventResponse> findAll() {
        List<RiskEventLog> riskEventLogs = riskEventLogRepository.findAll();
        return riskEventLogs
                .stream()
                .map(this::toDto)
                .toList();
    }

    private RiskEventResponse toDto(RiskEventLog rel) {
        RiskEventResponse response = new RiskEventResponse();

        response.setEventType(rel.getEventType());
        response.setRiskLevel(rel.getRiskLevel());
        response.setTargetIdentifier(rel.getTargetIdentifier());
        response.setActionTaken(rel.getActionTaken());
        response.setTriggerReason(rel.getTriggerReason());
        response.setTransactionAmount(rel.getTransactionAmount());
        response.setCreatedAt(rel.getCreatedAt());

        return response;
    }
}

package com.javaeasybank.risk.service;

import com.javaeasybank.risk.dto.RiskEventResponse;
import com.javaeasybank.risk.entity.RiskEventLog;
import com.javaeasybank.risk.repository.RiskEventLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiskEventService {

    private final RiskEventLogRepository riskEventLogRepository;

    public Page<RiskEventResponse> findAll(Pageable pageable) {
        return riskEventLogRepository.findAll(pageable)
                .map(this::toDto);
    }

    // 根據事件類型查詢
    public List<RiskEventResponse> findByEventType(String eventType) {
        return riskEventLogRepository.findByEventType(eventType)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // 根據採取的行動查詢 (補上你的 Repository 方法)
    public List<RiskEventResponse> findByActionTaken(String actionTaken) {
        return riskEventLogRepository.findByActionTaken(actionTaken)
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

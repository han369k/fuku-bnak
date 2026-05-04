package com.javaeasybank.risk.service;

import com.javaeasybank.risk.dto.RiskEventResponse;
import com.javaeasybank.risk.entity.RiskEventLog;
import com.javaeasybank.risk.repository.RiskEventLogRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RiskEventService {

    private final RiskEventLogRepository relRepos;

    public Page<RiskEventResponse> findAll(Pageable pageable) {
        return relRepos.findAll(pageable)
                .map(this::toDto);
    }

    public Page<RiskEventResponse> search(String eventType, String actionTaken, String riskLevel, Pageable pageable) {
        return relRepos.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(eventType)) {
                predicates.add(cb.equal(root.get("eventType"), eventType));
            }
            if (StringUtils.hasText(actionTaken)) {
                predicates.add(cb.equal(root.get("actionTaken"), actionTaken));
            }
            if (StringUtils.hasText(riskLevel)) {
                predicates.add(cb.equal(root.get("riskLevel"), riskLevel));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable).map(this::toDto);
    }

    private RiskEventResponse toDto(RiskEventLog rel) {
        RiskEventResponse response = new RiskEventResponse();
        response.setLogId(rel.getLogId());
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

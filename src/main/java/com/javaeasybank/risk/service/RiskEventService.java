package com.javaeasybank.risk.service;

import com.javaeasybank.risk.dto.ManualReviewEvent;
import com.javaeasybank.risk.dto.RiskEventResponse;
import com.javaeasybank.risk.entity.RiskEventLog;
import com.javaeasybank.risk.repository.ReviewTaskRepository;
import com.javaeasybank.risk.repository.RiskEventLogRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RiskEventService {

    private final RiskEventLogRepository relRepos;
    private final ReviewTaskRepository rtRepos;
    private final ObjectMapper objectMapper; // 用於將 Map 轉為 JSON 字串

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

    @Transactional
    public RiskEventLog recordEvent(ManualReviewEvent event) {
        RiskEventLog eventLog = new RiskEventLog();
        eventLog.setEventType(event.scene().name());
        eventLog.setBusinessId(event.businessId()); // 業務ID例貸款申請單 ID
        eventLog.setTargetIdentifier(event.target().getTargetIdentifier()); // 身分證或帳號
        eventLog.setRiskLevel(event.level()); // LOW, MEDIUM, HIGH
        eventLog.setActionTaken(event.disposition()); // PASS, REJECT, MANUAL_REVIEW
        eventLog.setTriggerReason(event.reason());

        // 關鍵：將 Metadata Map 轉換為 JSON 存入 nvarchar(max)
        try {
            String metadataJson = objectMapper.writeValueAsString(event.target().getRiskMetadata());
            eventLog.setMetaData(metadataJson);
        } catch (JacksonException e) {
            log.error("Metadata 轉換 JSON 失敗", e);
            eventLog.setMetaData("{}");
        }
        relRepos.save(eventLog);
        return eventLog;
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

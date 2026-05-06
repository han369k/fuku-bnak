package com.javaeasybank.risk.service;

import com.javaeasybank.risk.dto.ManualReviewEvent;
import com.javaeasybank.risk.dto.RiskEventResponse;
import com.javaeasybank.risk.entity.RiskEventLog;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RiskEventService {

    private final RiskEventLogRepository relRepos;
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

    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public RiskEventLog recordEvent(ManualReviewEvent event) {
        RiskEventLog eventLog = new RiskEventLog();
        eventLog.setEventType(event.scene().name());
        // 業務ID例貸款申請單 ID，若為 null 使用預設值，避免 DB NOT NULL 拋例外
        eventLog.setBusinessId(event.businessId() != null ? event.businessId() : "UNKNOWN");
        // target identifier 若為 null 使用預設值
        String targetId = (event.target() != null && event.target().getTargetIdentifier() != null)
                ? event.target().getTargetIdentifier()
                : "UNKNOWN";
        eventLog.setTargetIdentifier(targetId); // 身分證或帳號
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
        // 若 createdAt 尚未由 JPA Auditing 設定，則在此手動設定，避免 DB 不接受 NULL
        if (eventLog.getCreatedAt() == null) {
            eventLog.setCreatedAt(LocalDateTime.now());
        }

        try {
            relRepos.save(eventLog);
        } catch (Exception e) {
            log.error("寫入 RiskEventLog 失敗：event={}", event, e);
            throw e;
        }
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

package com.javaeasybank.risk.core;

import com.javaeasybank.common.exception.BusinessException;
import com.javaeasybank.risk.core.enums.Disposition;
import com.javaeasybank.risk.core.enums.RiskLevel;
import com.javaeasybank.risk.dto.ManualReviewEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
public abstract class BaseRiskHandler implements RiskHandler {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    /**
     * 提供子類別使用的標準發送方法
     */
    protected void publishEvent(RiskTarget target, String businessId, RiskLevel level, Disposition disposition,
            String reason) {
        // 嚴格檢查：businessId 為日誌關聯核心，不可為空
        if (businessId == null || businessId.isBlank()) {
            // 待修改
            throw new BusinessException("Risk BusinessId must not be null or empty");
        }

        if (eventPublisher == null) {
            log.error("事件發佈器未初始化，無法發送事件：scene={}, businessId={}", getScene(), businessId);
            throw new BusinessException("內部錯誤：無法發佈風控事件");
        }

        ManualReviewEvent event = new ManualReviewEvent(
                getScene(), businessId, target, level, disposition, reason);
        eventPublisher.publishEvent(event);
    }
}

package com.javaeasybank.risk.dto;

import com.javaeasybank.risk.core.enums.RiskLevel;
// 定義風險變更事件
public record RiskLevelChangedEvent(
        String customerId,
        RiskLevel newRiskLevel,
        String reason
) {
}

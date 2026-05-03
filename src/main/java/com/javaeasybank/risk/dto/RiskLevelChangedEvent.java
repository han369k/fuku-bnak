package com.javaeasybank.risk.dto;

import com.javaeasybank.risk.core.enums.AmlRiskLevel;
// 定義風險變更事件
public record RiskLevelChangedEvent(
        String customerId,
        AmlRiskLevel newRiskLevel,
        String reason
) {
}

package com.javaeasybank.loan.dto.requests;

import com.javaeasybank.risk.core.RiskTarget;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

// ===用戶申請===
@Getter
@Setter
public class LoanMemberRequestDTO implements RiskTarget {
    private String customerId;
    private String applyType;
    private Long applyAmount;
    private Integer applyPeriod;
    private BigDecimal rate;

    @Override
    public String getTargetIdentifier() {
        return customerId;
    }

    @Override
    public BigDecimal getAmount() {
        // 修正：介面定義是 BigDecimal，需要進行轉換
        return applyAmount != null ? BigDecimal.valueOf(applyAmount) : BigDecimal.ZERO;
    }
}

package com.javaeasybank.risk.dto.response;

import com.javaeasybank.risk.enums.RiskLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RiskCheckResponse {
    RiskLevel riskLevel;
    String reason;
}


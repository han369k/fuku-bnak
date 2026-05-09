package com.javaeasybank.risk.dto.request;

import com.javaeasybank.risk.core.enums.Disposition;
import com.javaeasybank.risk.core.enums.RiskLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CallbackRequest {

    private String businessId;
    private Disposition disposition;   // PASS / REJECT
    private RiskLevel riskLevel;
    private Long logId;
    private String reason;
}
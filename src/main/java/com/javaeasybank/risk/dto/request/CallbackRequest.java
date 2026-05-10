package com.javaeasybank.risk.dto.request;

import com.javaeasybank.risk.core.enums.Disposition;
import com.javaeasybank.risk.core.enums.RiskLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CallbackRequest {

    private String newStatus;     // APPROVED / REJECTED（不是 disposition enum）
    private String callerModule;  // 固定帶 "RISK"
    private String note;          // triggerReason
}
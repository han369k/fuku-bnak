package com.javaeasybank.risk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
public class RiskEventResponse {

    private Long logId;

    private String eventType;

    private String targetIdentifier;

    private String riskLevel;

    private String actionTaken;

    private String triggerReason;

    private BigDecimal transactionAmount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;

}

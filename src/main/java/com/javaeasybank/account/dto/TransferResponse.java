package com.javaeasybank.account.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransferResponse {
    private String referenceId;
    private BigDecimal fromAccountBalance;
    private BigDecimal toAccountBalance;
    private LocalDateTime transferredAt;
}

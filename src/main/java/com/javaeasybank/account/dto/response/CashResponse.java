package com.javaeasybank.account.dto.response;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CashResponse {

    // 交易編號
    private String referenceId;

    // 帳號
    private String accountNumber;

    // 金額
    private BigDecimal amount;

    // 交易時間
    private LocalDateTime transactedAt;
}

package com.javaeasybank.risk.dto.request;

import com.javaeasybank.risk.enums.BusinessScene;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskCheckRequest {

    @NotBlank(message = "客戶編號不可為空")
    private String customerId;

    @NotNull(message = "業務場景不可為空")
    private BusinessScene scene;

    private String businessId; // 業務主鍵（如轉帳序號），可選

    private BigDecimal amount; // 交易金額

    private String targetIdentifier; // 目標標識（如對手方帳號、IP 地址）

    private String callbackUrl; // 如果進入人工審核，審核完畢後的回調地址
}

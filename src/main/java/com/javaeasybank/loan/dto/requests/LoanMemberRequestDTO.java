package com.javaeasybank.loan.dto.requests;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

// ===用戶申請===
@Getter
@Setter
public class LoanMemberRequestDTO {
    private String applyType;
    private BigDecimal applyAmount;
    private Integer applyPeriod;
    private BigDecimal rate;
    // 客戶選擇的撥款入帳帳號（台幣活存），核准後撥款時使用
    // 對應 GET /api/customer/loan-repayments/debit-accounts 回傳的帳號
    private String disbursementAccount;
}

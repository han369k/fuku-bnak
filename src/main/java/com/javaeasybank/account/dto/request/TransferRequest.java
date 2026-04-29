package com.javaeasybank.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 轉帳請求 DTO，用於接收客戶端的轉帳資訊。
 */
@Data
public class TransferRequest {
    /**
     * 轉出帳號。
     */
    @NotBlank
    private String fromAccountNumber;
    
    /**
     * 轉入帳號。
     */
    @NotBlank
    private String toAccountNumber;
    
    /**
     * 轉帳金額，必須為正數。
     */
    @NotNull
    @Positive
    private BigDecimal amount;

    /**
     * 轉帳備註。
     */
    private String note;
}

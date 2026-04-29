package com.javaeasybank.account.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 轉帳響應 DTO，用於向客戶端返回轉帳結果資訊。
 */
@Data
public class TransferResponse {
    /**
     * 轉帳交易的參考 ID。
     */
    private String referenceId;
    /**
     * 轉出帳戶轉帳後的餘額。
     */
    private BigDecimal fromAccountBalance;
    /**
     * 轉入帳戶轉帳後的餘額。
     */
    private BigDecimal toAccountBalance;
    /**
     * 轉帳完成的時間。
     */
    private LocalDateTime transferredAt;
}

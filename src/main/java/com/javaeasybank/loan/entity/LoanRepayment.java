package com.javaeasybank.loan.entity;

import com.javaeasybank.loan.enums.LoanRepaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "LOAN_REPAYMENT")
@Getter
@Setter
@NoArgsConstructor
public class LoanRepayment {

    @Id
    private String repaymentId;             // repayment_id (PK)，前綴 "RP"

    private String accountId;              // account_id (FK → LOAN_ACCOUNT)

    // ── 期數資訊 ─────────────────────────────────────────────────────
    private Integer periodIndex;            // 第幾期（1-based，第 1 期 ~ 第 N 期）
    private LocalDate scheduledDate;        // 預計應繳日（建立時預排）
    private LocalDate paidDate;             // 實際繳款日（SCHEDULED / OVERDUE 時為 null）

    // ── 金額明細 ─────────────────────────────────────────────────────
    private BigDecimal totalAmount;         // 本期應繳總額（本金 + 利息）
    private BigDecimal principalPortion;    // 本期本金部分
    private BigDecimal interestPortion;     // 本期利息部分
    private BigDecimal remainingAfter;      // 繳完後剩餘本金（建立時預算，還款後更新）

    // ── 狀態 ─────────────────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    private LoanRepaymentStatus repaymentStatus; // repayment_status

    // ── 時間戳 ───────────────────────────────────────────────────────
    private LocalDateTime createTime;       // create_time
    private LocalDateTime updateTime;       // update_time（實際還款時更新）
}
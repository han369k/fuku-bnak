package com.javaeasybank.loan.entity;

import com.javaeasybank.loan.status.LoanApplicationStatus;
import com.javaeasybank.loan.status.LoanContactStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "LOAN_APPLICATION")
public class LoanApplication {

    @Id
    private String applicationId;          // application_id (PK)

    private Integer customerId;            // customer_id（會員 NULL）

    // 非會員另填
    private String applicantName;          // applicant_name
    private String applicantPhone;         // applicant_phone
    private String applicantEmail;         // applicant_email

    private String applyType;              // apply_type（貸款類型）
    private Long applyAmount;              // apply_amount
    private Integer applyPeriod;           // apply_period
    private BigDecimal rate;               // 系統計算利率

    @Enumerated(EnumType.STRING)
    private LoanApplicationStatus applicationStatus; // application_status

    private String empId;                  // 申請負責行員

    private LocalDateTime createTime;      // create_time

    @Enumerated(EnumType.STRING)
    private LoanContactStatus latestContactStatus;   // latest_contact_status

    private LocalDateTime latestContactTime;         // latest_contact_time
}

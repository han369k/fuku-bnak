package com.javaeasybank.loan.entity;

import com.javaeasybank.loan.enums.LoanContactChannel;
import com.javaeasybank.loan.enums.LoanContactStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "LOAN_CONTACT_LOG")
public class LoanContactLog {

    @Id
    private String logId;                  // log_id (PK)

    private String applicationId;          // application_id (FK)

    private String empId;                  // 聯繫人員

    @Enumerated(EnumType.STRING)
    private LoanContactStatus contactStatus;

    @Enumerated(EnumType.STRING)
    private LoanContactChannel contactChannel;

    private LocalDateTime contactTime;
    private String note;
}

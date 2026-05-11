package com.javaeasybank.loan.dto.response;

import com.javaeasybank.loan.enums.LoanContactChannel;
import com.javaeasybank.loan.enums.LoanContactStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class LoanContactLogResponseDTO {
    private String logId;
    private String applicationId;
    private String empId;
    private LoanContactStatus contactStatus;
    private LoanContactChannel contactChannel;
    private LocalDateTime contactTime;
    private String note;
}
package com.javaeasybank.loan.dto.response;

import com.javaeasybank.loan.enums.LoanDocumentType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LoanDocumentResponseDTO {

    private String documentId;
    private String applicationId;
    private LoanDocumentType documentType;
    private String fileUrl;
    private String originalName;
    private String uploadedBy;
    private LocalDateTime uploadTime;
    // 審核由風控負責，貸款模組不維護審核狀態
}

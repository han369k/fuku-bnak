package com.javaeasybank.loan.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 傳給風控模組的補件文件資訊
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanDocumentInfoDTO {
    private String documentId;
    private String documentType;   // LoanDocumentType enum 的 name()
    private String fileUrl;
    private String originalName;
}

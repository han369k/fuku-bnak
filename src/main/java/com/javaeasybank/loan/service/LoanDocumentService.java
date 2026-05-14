package com.javaeasybank.loan.service;

import com.javaeasybank.common.exception.BusinessException;
import com.javaeasybank.common.service.FileStorageService;
import com.javaeasybank.loan.dto.response.LoanDocumentResponseDTO;
import com.javaeasybank.loan.entity.LoanDocument;
import com.javaeasybank.loan.enums.LoanDocumentType;
import com.javaeasybank.loan.repository.LoanApplicationRepository;
import com.javaeasybank.loan.repository.LoanDocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/*
 * 補件文件管理（預留，補件功能暫未啟用）
 *   - 客戶上傳補件（MultipartFile → FileStorageService → 寫 loan_document）
 *   - 查詢指定申請的文件清單
 */
@Slf4j
@Service
@Transactional
public class LoanDocumentService {

    @Autowired
    private LoanDocumentRepository documentRepo;

    @Autowired
    private LoanApplicationRepository loanApplicationRepo;

    @Autowired
    private FileStorageService fileStorageService;

    // ===客戶上傳補件===

    public LoanDocumentResponseDTO upload(String applicationId,
                                          String customerId,
                                          String documentType,
                                          MultipartFile file) {

        loanApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new BusinessException("找不到申請編號：" + applicationId));

        LoanDocumentType type;
        try {
            type = LoanDocumentType.valueOf(documentType);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("不合法的文件類型：" + documentType);
        }

        String fileUrl = fileStorageService.store(file, "loan-documents/" + applicationId);

        LoanDocument doc = new LoanDocument();
        doc.setDocumentId(generateId("DOC"));
        doc.setApplicationId(applicationId);
        doc.setDocumentType(type);
        doc.setFileUrl(fileUrl);
        doc.setOriginalName(file.getOriginalFilename());
        doc.setUploadedBy(customerId);
        doc.setUploadTime(LocalDateTime.now());
        documentRepo.save(doc);

        log.info("[Document] 上傳完成 documentId={} applicationId={}", doc.getDocumentId(), applicationId);
        return toResponseDTO(doc);
    }

    // ===查詢===

    @Transactional(readOnly = true)
    public List<LoanDocumentResponseDTO> getByApplicationId(String applicationId) {
        return documentRepo.findByApplicationIdOrderByUploadTimeAsc(applicationId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ===工具方法===

    private String generateId(String prefix) {
        String timeStr      = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomSuffix = String.format("%04d", (int) (Math.random() * 10000));
        return prefix + timeStr + randomSuffix;
    }

    private LoanDocumentResponseDTO toResponseDTO(LoanDocument doc) {
        LoanDocumentResponseDTO dto = new LoanDocumentResponseDTO();
        dto.setDocumentId(doc.getDocumentId());
        dto.setApplicationId(doc.getApplicationId());
        dto.setDocumentType(doc.getDocumentType());
        dto.setFileUrl(doc.getFileUrl());
        dto.setOriginalName(doc.getOriginalName());
        dto.setUploadedBy(doc.getUploadedBy());
        dto.setUploadTime(doc.getUploadTime());
        return dto;
    }
}

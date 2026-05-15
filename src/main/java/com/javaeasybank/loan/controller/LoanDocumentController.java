package com.javaeasybank.loan.controller;

import com.javaeasybank.common.dto.response.ApiResponse;
import com.javaeasybank.common.exception.BusinessException;
import com.javaeasybank.common.util.JwtUtil;
import com.javaeasybank.loan.dto.response.LoanDocumentResponseDTO;
import com.javaeasybank.loan.service.LoanDocumentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/*
 * 補件文件入口
 *
 * 客戶端：
 *   POST   /api/loan-documents/{applicationId}/upload       - 上傳補件
 *   GET    /api/loan-documents/{applicationId}              - 查自己申請的文件清單
 *
 * 行員端：
 *   GET    /api/admin/loan-documents/{applicationId}        - 查任意申請的文件清單
 *   PATCH  /api/admin/loan-documents/{documentId}/review    - 審核補件（ACCEPTED / REJECTED）
 */
@RestController
@RequiredArgsConstructor
public class LoanDocumentController {

    private final LoanDocumentService loanDocumentService;
    private final JwtUtil jwtUtil;

    // ===客戶端===

    /**
     * 上傳補件（multipart/form-data）
     * 前端 FormData 欄位：documentType（字串）、file（檔案）
     */
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping(value = "/api/loan-documents/{applicationId}/upload",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<LoanDocumentResponseDTO>> upload(
            @PathVariable String applicationId,
            @RequestParam("documentType") String documentType,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {

        String customerId = extractCustomerId(request);
        LoanDocumentResponseDTO dto =
                loanDocumentService.upload(applicationId, customerId, documentType, file);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    /**
     * 查詢補件清單（客戶端）
     * 只能查看自己申請的文件
     */
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/api/loan-documents/{applicationId}")
    public ResponseEntity<ApiResponse<List<LoanDocumentResponseDTO>>> getMyDocs(
            @PathVariable String applicationId,
            HttpServletRequest request) {

        String customerId = extractCustomerId(request);
        return ResponseEntity.ok(ApiResponse.success(
                loanDocumentService.getByApplicationId(applicationId, customerId)));
    }

    /**
     * 查詢補件清單（行員端）
     * 可查任意申請的文件
     */
    @GetMapping("/api/admin/loan-documents/{applicationId}")
    public ResponseEntity<ApiResponse<List<LoanDocumentResponseDTO>>> getAdminDocs(
            @PathVariable String applicationId) {

        return ResponseEntity.ok(ApiResponse.success(
                loanDocumentService.getByApplicationId(applicationId)));
    }

    /**
     * 查詢可用的文件類型清單（客戶端 / 行員端共用）
     * 前端用來產生下拉選單，避免寫死
     */
    @GetMapping("/api/loan-documents/types")
    public ResponseEntity<ApiResponse<Map<String, String>>> getDocumentTypes() {
        Map<String, String> types = new java.util.LinkedHashMap<>();
        types.put("ID_CARD",         "身分證");
        types.put("INCOME_CERT",     "收入證明");
        types.put("EMPLOYMENT_CERT", "在職證明");
        types.put("BANK_STATEMENT",  "銀行存摺");
        types.put("PROPERTY_CERT",   "不動產謄本");
        types.put("TITLE_DEED",      "所有權狀");
        types.put("OTHER",           "其他");
        return ResponseEntity.ok(ApiResponse.success(types));
    }

    // Helper：從 Authorization Header 解析 customerId
    private String extractCustomerId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return jwtUtil.getCustomerIdFromToken(authHeader.substring(7));
        }
        throw new BusinessException("無法取得客戶身分資訊");
    }
}

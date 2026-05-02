package com.javaeasybank.loan.controller;

import com.javaeasybank.common.dto.response.ApiResponse;
import com.javaeasybank.loan.dto.requests.LoanMemberRequestDTO;
import com.javaeasybank.loan.dto.requests.LoanNonMemberRequestDTO;
import com.javaeasybank.loan.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// 客戶端：申請入口
@RestController
@RequestMapping("/api/loan-applications")
public class LoanApplicationController {

    @Autowired
    private LoanApplicationService loanApplicationService;

    // 會員申請
    @PostMapping("/member")
    public ResponseEntity<ApiResponse<String>> applyMember(
            @RequestBody LoanMemberRequestDTO dto) {
        String applicationId = loanApplicationService.insertMember(dto);
        return ResponseEntity.status(201).body(ApiResponse.success(applicationId));
    }

    // 非會員申請
    @PostMapping("/non-member")
    public ResponseEntity<ApiResponse<String>> applyNonMember(
            @RequestBody LoanNonMemberRequestDTO dto) {
        String applicationId = loanApplicationService.insertNonMember(dto);
        return ResponseEntity.status(201).body(ApiResponse.success(applicationId));
    }

    // 利率規則（申請表單載入時使用）
    @GetMapping("/rate-rules")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRateRules() {
        return ResponseEntity.ok(ApiResponse.success(loanApplicationService.getRateRules()));
    }
}

package com.javaeasybank.loan.controller;

import com.javaeasybank.common.dto.response.ApiResponse;
import com.javaeasybank.common.util.SessionUtil;
import com.javaeasybank.loan.dto.requests.LoanContactLogRequestDTO;
import com.javaeasybank.loan.dto.requests.LoanReviewDetailRequestDTO;
import com.javaeasybank.loan.dto.response.LoanApplicationResponseDTO;
import com.javaeasybank.loan.dto.response.LoanContactLogResponseDTO;
import com.javaeasybank.loan.dto.response.LoanReviewDetailResponseDTO;
import com.javaeasybank.loan.enums.LoanApplicationStatus;
import com.javaeasybank.loan.service.LoanApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 行員端：管理入口
@RestController
@RequestMapping("/api/admin")
public class LoanAdminController {

    @Autowired
    private LoanApplicationService loanApplicationService;

    // 依狀態查詢申請列表（預設 PENDING_CONTACT）
    @GetMapping
    public ResponseEntity<ApiResponse<List<LoanApplicationResponseDTO>>> getByStatus(
            @RequestParam(defaultValue = "PENDING_CONTACT") LoanApplicationStatus status,
            HttpSession session) {
        if (!SessionUtil.isAdminLoggedIn(session)) {
            return ResponseEntity.status(401).body(ApiResponse.fail("請先登入"));
        }
        return ResponseEntity.ok(ApiResponse.success(loanApplicationService.getByStatus(status)));
    }

    // 新增聯繫紀錄
    @PostMapping("/{id}/contact-logs")
    public ResponseEntity<ApiResponse<Void>> addContactLog(
            @PathVariable String id,
            @RequestBody LoanContactLogRequestDTO dto,
            HttpSession session) {
        if (!SessionUtil.isAdminLoggedIn(session)) {
            return ResponseEntity.status(401).body(ApiResponse.fail("請先登入"));
        }
        loanApplicationService.addContactLog(id, dto);
        return ResponseEntity.status(201).body(ApiResponse.success(null));
    }

    // 查詢聯繫紀錄
    @GetMapping("/{id}/contact-logs")
    public ResponseEntity<ApiResponse<List<LoanContactLogResponseDTO>>> getContactLogs(
            @PathVariable String id,
            HttpSession session) {
        if (!SessionUtil.isAdminLoggedIn(session)) {
            return ResponseEntity.status(401).body(ApiResponse.fail("請先登入"));
        }
        return ResponseEntity.ok(ApiResponse.success(loanApplicationService.getContactLogs(id)));
    }

    // 儲存二次填單草稿
    @PostMapping("/{id}/review")
    public ResponseEntity<ApiResponse<Void>> saveReview(
            @PathVariable String id,
            @RequestBody LoanReviewDetailRequestDTO dto,
            HttpSession session) {
        if (!SessionUtil.isAdminLoggedIn(session)) {
            return ResponseEntity.status(401).body(ApiResponse.fail("請先登入"));
        }
        loanApplicationService.saveReviewDetail(id, dto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 送審
    @PatchMapping("/{id}/review/submit")
    public ResponseEntity<ApiResponse<Void>> submitReview(
            @PathVariable String id,
            HttpSession session) {
        if (!SessionUtil.isAdminLoggedIn(session)) {
            return ResponseEntity.status(401).body(ApiResponse.fail("請先登入"));
        }
        loanApplicationService.submitReview(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 查詢填單內容
    @GetMapping("/{id}/review")
    public ResponseEntity<ApiResponse<LoanReviewDetailResponseDTO>> getReview(
            @PathVariable String id,
            HttpSession session) {
        if (!SessionUtil.isAdminLoggedIn(session)) {
            return ResponseEntity.status(401).body(ApiResponse.fail("請先登入"));
        }
        return ResponseEntity.ok(ApiResponse.success(loanApplicationService.getReviewDetail(id)));
    }
}

package com.javaeasybank.loan.service;

import com.javaeasybank.loan.dto.requests.LoanContactLogRequestDTO;
import com.javaeasybank.loan.dto.requests.LoanMemberRequestDTO;
import com.javaeasybank.loan.dto.requests.LoanNonMemberRequestDTO;
import com.javaeasybank.loan.dto.requests.LoanReviewDetailRequestDTO;
import com.javaeasybank.loan.dto.response.LoanApplicationResponseDTO;
import com.javaeasybank.loan.dto.response.LoanContactLogResponseDTO;
import com.javaeasybank.loan.dto.response.LoanReviewDetailResponseDTO;
import com.javaeasybank.loan.enums.LoanApplicationStatus;

import java.util.List;
import java.util.Map;

public interface LoanApplicationService {
    // ===查詢功能===
    // 依狀態顯示
    List<LoanApplicationResponseDTO> getByStatus(LoanApplicationStatus status);

    // ===新增功能===
    // 用戶申請
    String insertMember(LoanMemberRequestDTO dto);

    // 非用戶申請
    String insertNonMember(LoanNonMemberRequestDTO dto);

    // ===聯繫紀錄===
    // 新增聯繫紀錄，同步更新主表最新聯繫狀態
    void addContactLog(String applicationId, LoanContactLogRequestDTO dto);

    // 查某申請的所有聯繫紀錄
    List<LoanContactLogResponseDTO> getContactLogs(String applicationId);

    // ===二次填單===
    // 儲存草稿：有草稿就覆蓋同一筆，沒有就新建
    // 送審後不可修改
    void saveReviewDetail(String applicationId, LoanReviewDetailRequestDTO dto);

    // 送審：草稿 → SUBMITTED，主表推進為 PENDING_REVIEW
    // 申請狀態必須是 IN_CONTACT
    void submitReview(String applicationId);

    // 查填單內容
    LoanReviewDetailResponseDTO getReviewDetail(String applicationId);

    // 取得利率規則
    Map<String, Object> getRateRules();
}

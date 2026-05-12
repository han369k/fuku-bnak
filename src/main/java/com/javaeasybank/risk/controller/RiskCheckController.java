package com.javaeasybank.risk.controller;

import com.javaeasybank.common.dto.response.ApiResponse;
import com.javaeasybank.risk.dto.request.RiskCheckRequest;
import com.javaeasybank.risk.dto.response.RiskCheckResponse;
import com.javaeasybank.risk.enums.Disposition;
import com.javaeasybank.risk.enums.RiskLevel;
import com.javaeasybank.risk.enums.BusinessScene;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/risk/check")
public class RiskCheckController {

    /**
     * 交易風險檢查。
     * 在特定業務流程（如轉帳、登入、修改資料）執行前呼叫，用於實時判斷潛在風險。
     * 這類 API 屬於「同步檢查」，不產生人工審核任務，僅回傳風險評分或等級。
     * 1. 執行實時風控規則判斷。
     * 2. 記錄所有風險事件至 RISK_EVENT_LOG。
     * 3. 若判定為 MANUAL_REVIEW，則建立人工審核任務 (ReviewTask)。
     */
    @PostMapping
    public ResponseEntity<ApiResponse<RiskCheckResponse>> checkRisk(@Valid @RequestBody RiskCheckRequest request) {
        // 根據 request 中的 scene (例如 TRANSFER, LOGIN) 進行不同邏輯處理
        BusinessScene scene = request.getScene() != null ? request.getScene() : BusinessScene.TRANSFER;
        log.info("Processing risk check for customer: {}, scene: {}", request.getCustomerId(), request.getScene());

        log.info("Performing real-time risk check for scene: {}, customer: {}, amount: {}",
                scene, request.getCustomerId(), request.getAmount());
        // 1. 執行風險決策邏輯 (此處為模擬)
        RiskLevel level = RiskLevel.LOW;
        Disposition disposition = Disposition.PASS;
        String reason = "通過系統自動規則檢查";

        // 模擬邏輯：如果金額大於 100 萬，標記為 HIGH
        if (request.getAmount() != null && request.getAmount().doubleValue() > 1000000) {
            level = RiskLevel.HIGH;
            disposition = Disposition.MANUAL_REVIEW;
            reason = "交易金額超過閾值 (1,000,000)，需進入人工審核";
        } else if (request.getAmount() != null && request.getAmount().doubleValue() > 5000000) {
            level = RiskLevel.CRITICAL;
            disposition = Disposition.REJECT;
            reason = "交易金額過大，系統直接拒絕";
        }

        // 2. 建立並儲存 RiskEventLog (此處模擬 Service 操作)
        // RiskEventLog logEntity = new RiskEventLog();
        // logEntity.setEventType(request.getScene().name());
        // logEntity.setTargetIdentifier(request.getTargetIdentifier());
        // logEntity.setRiskLevel(level);
        // logEntity.setDisposition(disposition);
        // logEntity.setTriggerReason(reason);
        // logEntity.setTransactionAmount(request.getAmount());
        // riskEventLogRepository.save(logEntity);

        // 3. 如果需要人工審核，建立 ReviewTask
        Long reviewTaskId = null;
        if (disposition == Disposition.MANUAL_REVIEW) {
            log.info("Creating ReviewTask for businessId: {}", request.getBusinessId());
            // reviewTaskId = reviewTaskService.createTask(logEntity, request);
            reviewTaskId = 999L; // 模擬產生的 Task ID
        }

        RiskCheckResponse response = RiskCheckResponse.builder()
                .riskLevel(level)
                .reason(level == RiskLevel.LOW ? "通過實時規則校驗" : "觸發高額交易警戒")
                .reason(reason)
                .build();

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

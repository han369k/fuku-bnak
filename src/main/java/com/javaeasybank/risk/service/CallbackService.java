package com.javaeasybank.risk.service;

import com.javaeasybank.risk.core.enums.Disposition;
import com.javaeasybank.risk.dto.request.CallbackRequest;
import com.javaeasybank.risk.entity.RiskEventLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackService {

    private final RestTemplate restTemplate;

    /**
     * 通知來源模組審核結果
     * callbackUrl 由 RiskReviewRequest.callbackUrl 帶入
     * 例：http://loan-service/api/loan-callbacks/LOAN-001/status
     */
    public void notify(String callbackUrl, Disposition disposition, RiskEventLog eventLog) {

        CallbackRequest body = CallbackRequest.builder()
                .businessId(eventLog.getBusinessId())
                .disposition(disposition)
                .riskLevel(eventLog.getRiskLevel())
                .logId(eventLog.getLogId())
                .reason(eventLog.getTriggerReason())
                .build();

        log.info("[Callback] 通知 {} disposition={}", callbackUrl, disposition);

        try {
            ResponseEntity<Void> response =
                    restTemplate.postForEntity(callbackUrl, body, Void.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("[Callback] 回調失敗 url={} status={}",
                        callbackUrl, response.getStatusCode());
            }

        } catch (RestClientException e) {
            // callback 失敗不應該 rollback 風控本身的決策
            // 只記錄 log，後續可用排程補送
            log.error("[Callback] 無法連接來源模組 url={}", callbackUrl, e);
        }
    }
}
package com.javaeasybank.risk.service;

import com.javaeasybank.risk.core.enums.*;
import com.javaeasybank.risk.dto.request.RiskReviewRequest;
import com.javaeasybank.risk.dto.response.RiskReviewResponse;
import com.javaeasybank.risk.entity.CustomerCreditInfo;
import com.javaeasybank.risk.entity.ReviewTask;
import com.javaeasybank.risk.entity.RiskEventLog;
import com.javaeasybank.risk.repository.RiskEventLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiskReviewService {

    private final BlackListService blackListService;
    private final CreditSCoreService creditScoreService;
    private final ReviewTaskService reviewTaskService;
    private final CallbackService callbackService;
    private final RiskEventLogRepository logRepository;

    @Transactional
    public RiskReviewResponse process(RiskReviewRequest dto) {

        // 1. 黑名單批次檢查
        List<BlacklistType> hitTypes = blackListService.checkAll(buildBlacklistMap(dto));
        if (!hitTypes.isEmpty()) {
            log.info("[RiskReview] 命中黑名單 businessId={} hitTypes={}",
                    dto.getBusinessId(), hitTypes);
            RiskEventLog log = buildAndSaveLog(
                    dto, null, 0,
                    Disposition.REJECT,
                    "命中黑名單：" + hitTypes);
            // 黑名單直接 callback，不需要人審
            callbackService.notify(dto.getCallbackUrl(), Disposition.REJECT, log);
            return buildResponse(log, Disposition.REJECT, null);
        }

        // 2. 同步信用資料並重新評分
        //    - 客戶不存在 → initializeCreditInfo 建立 mock 資料
        //    - 客戶已存在 → 覆蓋選填欄位後 rescore
        CustomerCreditInfo credit = syncCredit(dto);

        // 3. 依 finalScore 決定 Disposition
        //    CreditSCoreService 的 resolveRiskLevel：
        //    ≥ 70 → LOW, 40~69 → MEDIUM, < 40 → HIGH
        //    對應送審閾值：LOW → PASS, MEDIUM → MANUAL_REVIEW, HIGH → REJECT
        Disposition disposition = resolveDisposition(credit);

        log.info("[RiskReview] businessId={} finalScore={} riskLevel={} disposition={}",
                dto.getBusinessId(), credit.getFinalScore(),
                credit.getRiskLevel(), disposition);

        // 4. 存 RiskEventLog + 後續動作（同一個 @Transactional 保護）
        return handleDisposition(dto, credit, disposition);
    }

    // ── 私有方法 ─────────────────────────────────────────────────────────

    /**
     * 從 dto 組黑名單比對 Map
     * null 的欄位不放入，避免 checkAll 誤判
     */
    private Map<BlacklistType, String> buildBlacklistMap(RiskReviewRequest dto) {
        Map<BlacklistType, String> map = new EnumMap<>(BlacklistType.class);
        if (dto.getIdCard() != null) map.put(BlacklistType.ID_CARD, dto.getIdCard());
        if (dto.getPhone() != null) map.put(BlacklistType.PHONE, dto.getPhone());
        if (dto.getEmail() != null) map.put(BlacklistType.EMAIL, dto.getEmail());
        return map;
    }

    /**
     * 信用資料同步：
     * - 新客戶（無資料）→ initializeCreditInfo 建立並評分
     * - 舊客戶（有資料）→ 選填欄位有傳就覆蓋，再 rescore
     */
    private CustomerCreditInfo syncCredit(RiskReviewRequest dto) {

        // 不再處理新客戶建立，找不到直接拋例外
        // 代表 Customer 模組尚未完成初始化，屬於異常情況
        creditScoreService.updateIfPresent(dto);
        return creditScoreService.rescore(dto.getCustomerId());
    }

    /**
     * RiskLevel → Disposition 對應規則
     * LOW    → PASS          自動通過
     * MEDIUM → MANUAL_REVIEW 轉人工審核
     * HIGH   → REJECT        自動拒絕
     */
    private Disposition resolveDisposition(CustomerCreditInfo credit) {
        return switch (credit.getRiskLevel()) {
            case LOW -> Disposition.PASS;
            case MEDIUM -> Disposition.MANUAL_REVIEW;
            case HIGH -> Disposition.REJECT;
            default -> throw new IllegalStateException("未知的風險等級: " + credit.getRiskLevel());

        };
    }

    /**
     * 存 Log 後依 Disposition 分流
     * - PASS / REJECT    → 立刻 callback
     * - MANUAL_REVIEW    → 建 ReviewTask，等人工決策後再 callback
     */
    @Transactional
    private RiskReviewResponse handleDisposition(
            RiskReviewRequest dto,
            CustomerCreditInfo credit,
            Disposition disposition) {

        RiskEventLog log = buildAndSaveLog(
                dto, credit,
                credit.getFinalScore(),
                disposition, null);

        return switch (disposition) {
            case PASS, REJECT -> {
                callbackService.notify(dto.getCallbackUrl(), disposition, log);
                yield buildResponse(log, disposition, null);
            }
            case MANUAL_REVIEW -> {
                ReviewTask task = reviewTaskService.create(log, dto);
                yield buildResponse(log, disposition, task.getTaskId());
            }
        };
    }

    private RiskEventLog buildAndSaveLog(
            RiskReviewRequest dto,
            CustomerCreditInfo credit,
            int finalScore,
            Disposition disposition,
            String overrideReason) {

        RiskEventLog log = new RiskEventLog();
        log.setEventType(dto.getScene().name());
        log.setBusinessId(dto.getBusinessId());
        log.setTargetIdentifier(dto.getCustomerId());
        log.setRiskLevel(credit != null ? credit.getRiskLevel() : RiskLevel.HIGH);
        log.setDisposition(disposition);
        log.setTransactionAmount(dto.getAmount());
        log.setTriggerReason(overrideReason != null
                ? overrideReason
                : buildReason(credit, finalScore));
        log.setMetaData(credit != null ? buildMetaData(credit) : null);

        return logRepository.save(log);
    }

    private String buildReason(CustomerCreditInfo credit, int finalScore) {
        if (credit == null) return null;
        return String.format("finalScore=%d riskLevel=%s occupation=%s",
                finalScore, credit.getRiskLevel(), credit.getOccupation());
    }

    /**
     * 把關鍵評分依據序列化進 metaData（方便事後查核）
     * 對應 RiskEventLog.metaData 欄位
     */
    private String buildMetaData(CustomerCreditInfo credit) {
        return String.format(
                "{\"externalScore\":%d,\"annualIncome\":%s," +
                        "\"otherBankDebt\":%s,\"hasRealEstate\":%b}",
                credit.getExternalScore(),
                credit.getAnnualIncome(),
                credit.getOtherBankDebt(),
                credit.getHasRealEstate());
    }

    private RiskReviewResponse buildResponse(
            RiskEventLog log,
            Disposition disposition,
            Long reviewTaskId) {

        return RiskReviewResponse.builder()
                .logId(log.getLogId())
                .businessId(log.getBusinessId())
                .disposition(disposition)
                .riskLevel(log.getRiskLevel())
                .finalScore(log.getMetaData() != null
                        ? extractFinalScore(log) : 0)
                .reason(log.getTriggerReason())
                .reviewTaskId(reviewTaskId)
                .build();
    }

    private int extractFinalScore(RiskEventLog log) {
        // metaData 是 JSON 字串，簡單取值避免引入額外依賴
        // 正式專案建議改用 ObjectMapper
        try {
            String meta = log.getMetaData();
            // {"externalScore":..., "finalScore":72, ...}
            // 這裡 finalScore 其實存在 credit 物件，
            // buildResponse 呼叫鏈有 credit 時可直接傳入，這裡示意
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
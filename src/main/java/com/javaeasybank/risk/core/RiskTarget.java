package com.javaeasybank.risk.core;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

/**
 * 風控目標介面
 * 任何需要經過風控檢查的 Request DTO 都必須實作此介面，
 * 讓 AOP 切面能統一取得「目標識別碼」與「交易金額」。
 */
public interface RiskTarget {

    /**
     * 取得觸發風控的目標識別碼 (對應資料庫的 TargetIdentifier)
     * 例如：轉出帳號、信用卡號、用戶身分證字號等。
     */
   String getTargetIdentifier();

    /**
     * 取得本次交易的金額
     * 若該業務場景無金額 (如：登入防護)，可回傳 BigDecimal.ZERO
     */
    BigDecimal getAmount();

    default Map<String,Object> getRiskMetadata() {
        return Collections.emptyMap();
    }
}

package com.javaeasybank.risk.core;

import com.javaeasybank.risk.core.enums.RiskScene;

public interface RiskHandler {
    // 判斷這個 Handler 是否支援該場景
    RiskScene getScene();

    // 執行的風險檢查邏輯
    void handle(RiskTarget target);
    /**
     * 從方法參數中解析出 RiskTarget。
     * 若無法解析（例如參數不合法），回傳 null，切面會跳過此次檢查。
     */
    RiskTarget resolve(Object[] args);
}
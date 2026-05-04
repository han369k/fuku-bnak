package com.javaeasybank.risk.core;

import com.javaeasybank.risk.core.enums.RiskScene;

import java.math.BigDecimal;

public interface RiskHandler {
    // 判斷這個 Handler 是否支援該場景
    RiskScene getScene();

    // 執行的風險檢查邏輯
    void handle(RiskTarget target);
}
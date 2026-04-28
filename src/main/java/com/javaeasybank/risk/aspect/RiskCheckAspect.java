package com.javaeasybank.risk.aspect;

import com.javaeasybank.risk.annotation.RiskCheck;
import com.javaeasybank.risk.core.RiskTarget;
import com.javaeasybank.risk.repository.RiskEventLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Aspect // 宣告這是一個切面 (攔截器)
@Component
@Slf4j
@RequiredArgsConstructor
public class RiskCheckAspect {

    // @Before 代表在目標方法執行「之前」觸發
    // "@annotation(riskCheck)" 代表攔截所有標有 @RiskCheck 的方法，並把標註物件傳進來
    private final RiskEventLogRepository relRepos;

    @Around("@annotation(riskCheck)")
    public Object monitorRisk(ProceedingJoinPoint joinPoint, RiskCheck riskCheck) throws Throwable {

        // 1. 取得你定義的場景 (例如 "TRANSFER")
        String scene = riskCheck.scene();

        // 2. 取得該方法傳入的所有參數
        Object[] args = joinPoint.getArgs();

        // 3. 尋找是否有實作 RiskTarget 介面的 Request DTO
        for (Object arg : args) {
            if (arg instanceof RiskTarget) {
                RiskTarget target = (RiskTarget) arg;

                String identifier = target.getTargetIdentifier();
                BigDecimal amount = target.getAmount();

                log.info("【風控引擎啟動】場景: {}, 目標: {}, 金額: {}", scene, identifier, amount);

                // --- 4. 你的風控規則 (Day 1 測試版：單筆不可大於 50 萬) ---
                if (amount != null && amount.compareTo(new BigDecimal("500000")) > 0) {

                    log.warn("【風控攔截】觸發大額交易限制！");

                    // 寫入資料庫
                    //saveRiskLog(scene, identifier, "HIGH", "BLOCKED", "單筆交易金額超過 50 萬", amount);

                    // 直接拋出 Exception 擋下交易，組員的轉帳代碼絕對不會被執行！
                    throw new RuntimeException("風控拒絕：單筆金額超過上限！");
                }
            }
        }

        // 5. 如果檢查都沒問題，放行讓程式繼續執行
        return joinPoint.proceed();
    }
}

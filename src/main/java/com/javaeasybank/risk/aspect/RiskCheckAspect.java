package com.javaeasybank.risk.aspect;

import com.javaeasybank.risk.annotation.RiskCheck;
import com.javaeasybank.risk.core.RiskHandler;
import com.javaeasybank.risk.core.enums.RiskScene;
import com.javaeasybank.risk.core.RiskTarget;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect // 宣告這是一個切面 (攔截器)
@Component
@Slf4j
public class RiskCheckAspect {

    // Key 是 Enum，Value 是對應的處理實作
    private final Map<RiskScene, RiskHandler> handlerMap;

    // 利用建構子注入，並直接轉換為 Map
    public RiskCheckAspect(List<RiskHandler> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(RiskHandler::getScene, h -> h));
    }

    @Around("@annotation(riskCheck)")
    public Object monitorRisk(ProceedingJoinPoint joinPoint, RiskCheck riskCheck) throws Throwable {

        RiskScene scene = riskCheck.scene();
        Object[] args = joinPoint.getArgs();
        RiskHandler handler = handlerMap.get(scene);

        if (handler != null && args.length > 0) {
            RiskTarget target = null;

            // 情況 A：參數本身就實作了 RiskTarget (例如 createCustomer 的 Request DTO)
            if (args[0] instanceof RiskTarget rt) {
                target = rt;
            }
            // 情況 B：參數是 String (例如 updateCustomer 的 customerId)，手動封裝
            else if (args[0] instanceof String id) {
                target = new RiskTarget() {
                    @Override
                    public String getTargetIdentifier() {
                        return id;
                    }

                    @Override
                    public BigDecimal getAmount() {
                        return BigDecimal.ZERO;
                    }
                };
            }

            if (target != null) {
                handler.handle(target); // 呼叫修改後的 handler
            }
        }

        return joinPoint.proceed();
    }
}

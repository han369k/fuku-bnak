package com.javaeasybank.risk.core.aspect;

import com.javaeasybank.risk.core.annotation.RiskCheck;
import com.javaeasybank.risk.core.RiskHandler;
import com.javaeasybank.risk.core.enums.BusinessScene;
import com.javaeasybank.risk.core.RiskTarget;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect // 宣告這是一個切面 (攔截器)
@Component
@Slf4j
@Order(1)
public class RiskCheckAspect {

    // Key 是 Enum，Value 是對應的處理實作
    private final Map<BusinessScene, RiskHandler> handlerMap;

    // 利用建構子注入，並直接轉換為 Map
    public RiskCheckAspect(List<RiskHandler> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(RiskHandler::getScene, h -> h));
    }

    @Pointcut("@annotation(com.javaeasybank.risk.core.annotation.RiskCheck)")
    public void riskCheckPointcut() {}

    @Around("riskCheckPointcut() && @annotation(riskCheck)")
    public Object monitorRisk(ProceedingJoinPoint joinPoint,
                              RiskCheck riskCheck) throws Throwable {

        log.debug("Risk check triggered: scene={}", riskCheck.scene());

        BusinessScene scene = riskCheck.scene();
        RiskHandler handler = handlerMap.get(scene);

        if (handler != null) {
            RiskTarget target = handler.resolve(joinPoint.getArgs());
            if (target != null) {
                handler.handle(target);
            } else {
                log.warn("Risk target resolved to null: scene={}", scene);
            }
        }

        return joinPoint.proceed();
    }
}

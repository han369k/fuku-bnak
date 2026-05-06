package com.javaeasybank.risk.handler;

import com.javaeasybank.common.exception.BusinessException;
import com.javaeasybank.risk.core.RiskHandler;
import com.javaeasybank.risk.core.RiskTarget;
import com.javaeasybank.risk.core.enums.BlacklistType;
import com.javaeasybank.risk.core.KYC;
import com.javaeasybank.risk.core.enums.BusinessScene;
import com.javaeasybank.risk.service.BlackListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CustomerCreateHandler implements RiskHandler {

    private final BlackListService blService;

    public CustomerCreateHandler(BlackListService blService) {
        this.blService = blService;
    }

    @Override
    public BusinessScene getScene() {
        return BusinessScene.CREATE_CUSTOMER;
    }

    @Override
    public void handle(RiskTarget target) {
        // 1. 取得所有擴充欄位
        Map<String, Object> metadata = target.getRiskMetadata();

        List<String> failedDescriptions = new ArrayList<>();

        // 2. 定義需要比對的維度與對應的黑名單類型
        // Map.of(MetadataKey, BlacklistType)
        Map<String, BlacklistType> checkMap = Map.of(
                KYC.ID_CARD, BlacklistType.ID_CARD,
                KYC.PHONE, BlacklistType.PHONE,
                KYC.EMAIL, BlacklistType.EMAIL
        );

        for (Map.Entry<String, BlacklistType> entry : checkMap.entrySet()) {
            Object value = metadata.get(entry.getKey());
            if (value instanceof String valStr && !valStr.isBlank()) {
                if (blService.isBlacklisted(entry.getValue(), valStr)) {
                    // 2. 發現異常不立刻拋出，先記下來
                    failedDescriptions.add(entry.getValue().getDescription());
                }
            }
        }
        if (!failedDescriptions.isEmpty()) {
            String errorMsg = String.format("風險控管攔截：您的 (%s) 存在異常，請聯繫客服。",
                    String.join("、", failedDescriptions));
            // 結果會像：風險控管攔截：您的 (身分證字號、手機號碼) 存在異常...
            throw new BusinessException(errorMsg);
        }
    }

    @Override
    public RiskTarget resolve(Object[] args) {
        if (args == null || args.length == 0) {
            log.warn("CustomerCreateHandler: no args");
            return null;
        }
        if (args[0] instanceof RiskTarget rt) {
            return rt;
        }
        log.warn("CustomerCreateHandler: args[0] is not RiskTarget, got: {}",
                args[0] == null ? "null" : args[0].getClass().getSimpleName());
        return null;
    }

}

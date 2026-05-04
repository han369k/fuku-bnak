package com.javaeasybank.risk.handler;

import com.javaeasybank.common.exception.BusinessException;
import com.javaeasybank.risk.core.RiskHandler;
import com.javaeasybank.risk.core.RiskTarget;
import com.javaeasybank.risk.core.enums.BlacklistType;
import com.javaeasybank.risk.core.enums.RiskScene;
import com.javaeasybank.risk.service.BlackListService;

public class CustomerCreateHandler implements RiskHandler {

    private final BlackListService blService;

    public CustomerCreateHandler(BlackListService blService) {
        this.blService = blService;
    }

    @Override
    public RiskScene getScene() {
        return RiskScene.CREATE_CUSTOMER;
    }

    public void handle(RiskTarget target) {
        String idNumber = target.getTargetIdentifier();
        if (blService.isBlacklisted(BlacklistType.ID_CARD, idNumber)) {
            throw new BusinessException("風險控管：此身分證字號已被列入黑名單");
        }
    }

}

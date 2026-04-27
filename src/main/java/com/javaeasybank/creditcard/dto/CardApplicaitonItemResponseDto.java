package com.javaeasybank.creditcard.dto;

import java.math.BigDecimal;

public record CardApplicaitonItemResponseDto(
    Integer itemId,
    String cardName,
    String result,
    BigDecimal approvedLimit,
    BigDecimal annualFee,
    Boolean createCardFlag,
    String remark
) {
}
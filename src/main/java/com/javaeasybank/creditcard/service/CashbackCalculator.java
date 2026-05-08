package com.javaeasybank.creditcard.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.javaeasybank.creditcard.entity.CreditCard;

@Service
public class CashbackCalculator {

    public BigDecimal calculateCashback(BigDecimal amount, CreditCard card) {
     
        BigDecimal rate = card.getCardType().getCashbackRate();

        if (rate == null) {
            return BigDecimal.ZERO;
        }
        return amount.multiply(rate).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
        
        
    }
}

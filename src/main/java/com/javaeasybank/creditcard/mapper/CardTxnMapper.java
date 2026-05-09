package com.javaeasybank.creditcard.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.javaeasybank.creditcard.dto.CardTxnResponseDto;
import com.javaeasybank.creditcard.entity.CardTransaction;

@Component
public class CardTxnMapper {

    public CardTxnResponseDto toDto(CardTransaction txn) {
        if (txn == null) {
            return null;
        }

        CardTxnResponseDto dto = new CardTxnResponseDto();
        dto.setTxnId(txn.getTxnId());
        dto.setTxnAmount(txn.getTxnAmount());
        dto.setTxnType(txn.getTxnType());
        dto.setTxnDate(txn.getTxnDate());
        dto.setDescription(txn.getDescription());
        dto.setCashbackRate(txn.getCashbackRate());
        dto.setCashbackAmount(txn.getCashbackAmount());

        if (txn.getMerchant() != null) {
            dto.setMerchantName(txn.getMerchant().getMerchantName());
        }

        if (txn.getCard() != null) {
            dto.setCardNumber(maskCard(txn.getCard().getCardNumber()));
            if (txn.getCard().getCustomer() != null) {
                dto.setCustomerName(txn.getCard().getCustomer().getName());
            }
        }

        if (txn.getRefTxn() != null) {
            dto.setRefTxnId(txn.getRefTxn().getTxnId());
        }

        return dto;
    }

    public List<CardTxnResponseDto> toDtoList(List<CardTransaction> list) {
        if (list == null) {
            return null;
        }
        return list.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public String maskCard(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return cardNumber;
        }
        return cardNumber.substring(0, 4)
            + " **** **** "
            + cardNumber.substring(cardNumber.length() - 4);
    }
}

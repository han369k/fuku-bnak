package com.javaeasybank.creditcard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaeasybank.common.exception.BusinessException;
import com.javaeasybank.creditcard.dto.CardTxnRequestDto;
import com.javaeasybank.creditcard.dto.CardTxnResponseDto;
import com.javaeasybank.creditcard.entity.CardTransaction;
import com.javaeasybank.creditcard.mapper.CardTxnMapper;
import com.javaeasybank.creditcard.repository.CardTxnRepository;
import com.javaeasybank.creditcard.repository.CreditCardRepository;
import com.javaeasybank.creditcard.repository.MerchantRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CardTxnService {

    private final CardTxnRepository cardTxnRepository;
    private final CreditCardRepository cardRepository;
    private final MerchantRepository merchantRepository;
    private final CardTxnMapper mapper;

    // 新增交易
    @Transactional
    public CardTxnResponseDto create(CardTxnRequestDto dto) {
        CardTransaction txn = new CardTransaction();
        txn.setTxnAmount(dto.getTxnAmount());
        txn.setTxnType(dto.getTxnType());
        txn.setDescription(dto.getDescription());

        txn.setCard(cardRepository.findById(dto.getCardId())
                .orElseThrow(() -> new BusinessException("Card not found")));

        txn.setMerchant(merchantRepository.findById(dto.getMerchantId())
                .orElseThrow(() -> new BusinessException("Merchant not found")));

        CardTransaction saved = cardTxnRepository.save(txn);

        return mapper.toDto(saved);
    }
    // 查全部交易（DTO）
    public List<CardTxnResponseDto> findAll() {
        return mapper.toDtoList(cardTxnRepository.findAll());
    }
    // 查單筆交易（DTO）
    public CardTxnResponseDto findById(Integer id) {
        return mapper.toDto(cardTxnRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Transaction not found")));
    }
    // 更新交易
    @Transactional
    public CardTxnResponseDto update(Integer id, CardTxnRequestDto dto) {
        CardTransaction txn = cardTxnRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Transaction not found"));

        txn.setTxnAmount(dto.getTxnAmount());
        txn.setTxnType(dto.getTxnType());
        txn.setDescription(dto.getDescription());

        if (dto.getCardId() != null) {
            txn.setCard(cardRepository.findById(dto.getCardId())
                    .orElseThrow(() -> new BusinessException("Card not found")));
        }

        if (dto.getMerchantId() != null) {
            txn.setMerchant(merchantRepository.findById(dto.getMerchantId())
                    .orElseThrow(() -> new BusinessException("Merchant not found")));
        }

        CardTransaction saved = cardTxnRepository.save(txn);
        return mapper.toDto(saved);
    }
    // 刪除交易
    @Transactional
    public void deleteById(Integer id) {
        if (!cardTxnRepository.existsById(id)) {
            throw new BusinessException("Transaction not found");
        }
        cardTxnRepository.deleteById(id);
    }
}

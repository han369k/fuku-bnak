package com.javaeasybank.creditcard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.javaeasybank.common.exception.BusinessException;
import com.javaeasybank.creditcard.dto.CreditCardRequestDto;
import com.javaeasybank.creditcard.dto.CreditCardResponseDto;
import com.javaeasybank.creditcard.entity.CreditCard;
import com.javaeasybank.creditcard.mapper.CreditCardMapper;
import com.javaeasybank.creditcard.repository.CardAppItemRepository;
import com.javaeasybank.creditcard.repository.CardTypeRepository;
import com.javaeasybank.creditcard.repository.CreditCardRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CreditCardService {

	private final CreditCardRepository cardRepository;
    private final CardTypeRepository cardTypeRepository;
    private final CardAppItemRepository itemRepository;
    private final CreditCardMapper mapper;

    // 查全部（回 DTO）
    public List<CreditCardResponseDto> findAll() {
        return mapper.toDtoList(cardRepository.findAll());
    }

    // 查單筆
    public CreditCardResponseDto findById(Integer id) {
        CreditCard entity = cardRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Can not found the creditcard ID:" + id));

        return mapper.toDto(entity);
    }

    // 新增
    public CreditCardResponseDto create(CreditCardRequestDto dto) {

        CreditCard entity = mapper.toEntity(dto);

        // 關聯處理
        entity.setCardType(
            cardTypeRepository.findById(dto.getCardTypeId())
                .orElseThrow(() -> new BusinessException("CardType not found"))
        );

        entity.setApplicationItem(
            itemRepository.findById(dto.getApplicationItemId())
                .orElseThrow(() -> new BusinessException("ApplicationItem not found"))
        );

        return mapper.toDto(cardRepository.save(entity));
    }

    // 更新
    public CreditCardResponseDto update(Integer id, CreditCardRequestDto dto) {

        CreditCard entity = cardRepository.findById(id)
            .orElseThrow(() -> new BusinessException("CreditCard not found"));

        // 基本欄位更新
        mapper.updateEntityFromDto(dto, entity);

        // 關聯更新
        if (dto.getCardTypeId() != null) {
            entity.setCardType(
                cardTypeRepository.findById(dto.getCardTypeId())
                    .orElseThrow(() -> new BusinessException("CardType not found"))
            );
        }

        if (dto.getApplicationItemId() != null) {
            entity.setApplicationItem(
                itemRepository.findById(dto.getApplicationItemId())
                    .orElseThrow(() -> new BusinessException("ApplicationItem not found"))
            );
        }

        return mapper.toDto(cardRepository.save(entity));
    }

    // 刪除
    public void deleteById(Integer id) {
        cardRepository.deleteById(id);
    }
	//尚未有Customer Repo先不啟用
	// public List<CreditCard> findByCustomerId(Integer customerId) {
	// 	return creditCardRepository.findByCustomer_CustomerId(customerId);
	// }
	
	
}

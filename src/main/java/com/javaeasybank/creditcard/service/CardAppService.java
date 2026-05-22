package com.javaeasybank.creditcard.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaeasybank.common.exception.BusinessException;
import com.javaeasybank.creditcard.dto.CardApplicationRequestDto;
import com.javaeasybank.creditcard.dto.CardApplicationResponseDto;
import com.javaeasybank.creditcard.entity.CardAccount;
import com.javaeasybank.creditcard.entity.CardApplication;
import com.javaeasybank.creditcard.entity.CardApplicationItem;
import com.javaeasybank.creditcard.entity.CardType;
import com.javaeasybank.creditcard.enums.CardApplicationItemResult;
import com.javaeasybank.creditcard.enums.CardApplicationStatus;
import com.javaeasybank.creditcard.mapper.CardApplicationMapper;
import com.javaeasybank.creditcard.repository.CardAccountRepository;
import com.javaeasybank.creditcard.repository.CardAppItemRepository;
import com.javaeasybank.creditcard.repository.CardAppRepository;
import com.javaeasybank.creditcard.repository.CardTypeRepository;
import com.javaeasybank.customer.entity.CustomerProfile;
import com.javaeasybank.customer.repository.CustomerProfileRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class CardAppService {

    private static final BigDecimal DEFAULT_CREDIT_LIMIT = new BigDecimal("100000");

    private final CardAppRepository cardAppRepository;
    private final CardAppItemRepository cardAppItemRepository;
    private final CardApplicationMapper cardApplicationMapper;
    private final CustomerProfileRepository customerRepository;
    private final CardTypeRepository cardTypeRepository;
    private final CardAccountRepository cardAccountRepository;

    // 查全部
    public Page<CardApplicationResponseDto> findAll(Pageable pageable) {
        return cardAppRepository.findAll(pageable)
                .map(this::toDtoWithItem);

    }

    // 查單筆（DTO）
    public CardApplicationResponseDto findById(Integer id) {
        return toDtoWithItem(getEntityById(id));
    }

    // 內部用（Entity）
    private CardApplication getEntityById(Integer id) {
        return cardAppRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Credit card application not found."));
    }

    // 刪除
    public void deleteById(Integer id) {
        CardApplication app = getEntityById(id);
        cardAppRepository.delete(app);
    }

    // 新增
    public CardApplicationResponseDto create(CardApplicationRequestDto requestDto) {
        if (requestDto.getCardTypeId() == null) {
            throw new BusinessException("Card type is required");
        }

        boolean exists = cardAppItemRepository
                .existsByApplication_Customer_CustomerIdAndCardType_CardTypeIdAndApplication_Status(
                        requestDto.getCustomerId(),
                        requestDto.getCardTypeId(),
                        CardApplicationStatus.PENDING);

        if (exists) {
            throw new BusinessException("你已申辦過該卡片");
        }

        CardApplication entity = cardApplicationMapper.toEntity(requestDto);

        entity.setStatus(CardApplicationStatus.PENDING);

        CustomerProfile customer = customerRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new BusinessException("Customer not found"));
        entity.setCustomer(customer);

        CardApplication saved = cardAppRepository.save(entity);

        CardType cardType = cardTypeRepository.findById(requestDto.getCardTypeId())
                .orElseThrow(() -> new BusinessException("Card type not found"));
        CardApplicationItem item = new CardApplicationItem();
        item.setApplication(saved);
        item.setCardType(cardType);
        item.setResult(CardApplicationItemResult.PENDING);

        // 預設額度
        BigDecimal approvedLimit = cardAccountRepository
                .findByCustomer_CustomerId(requestDto.getCustomerId())
                .map(CardAccount::getCreditLimit)
                .orElse(DEFAULT_CREDIT_LIMIT);

        item.setApprovedLimit(approvedLimit);

        // 預設年費
        item.setAnnualFee(cardType.getAnnualFee());

        cardAppItemRepository.save(item);

        return toDtoWithItem(saved);
    }

    // // 更新狀態 已註解 透過Item的Result來判斷狀態
    // public CardApplicationResponseDto updateStatus(Integer id,
    // CardApplicationStatus status) {
    // CardApplication app = getEntityById(id);

    // app.setStatus(status);

    // CardApplication saved = cardAppRepository.save(app);

    // return toDtoWithItem(saved);
    // }

    public CardApplicationResponseDto updateRemark(Integer id, String remark) {
        CardApplication app = cardAppRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Card application not found"));

        app.setRemark(remark);
        return toDtoWithItem(cardAppRepository.save(app));
    }

    public Page<CardApplicationResponseDto> search(Pageable pageable, String keyword, CardApplicationStatus status) {
        return cardAppRepository.search(
                pageable,
                keyword,
                status).map(this::toDtoWithItem);
    }

    public Page<CardApplicationResponseDto> findMyApplications(String customerId, Pageable pageable) {
        return cardAppRepository
                .findByCustomer_CustomerId(customerId, pageable)
                .map(this::toDtoWithItem);
    }

    private CardApplicationResponseDto toDtoWithItem(CardApplication app) {
        CardApplicationResponseDto dto = cardApplicationMapper.toDto(app);

        cardAppItemRepository.findByApplicationApplicationId(app.getApplicationId())
                .stream()
                .findFirst()
                .ifPresent(item -> {
                    CardType cardType = item.getCardType();
                    if (cardType != null) {
                        dto.setCardTypeId(cardType.getCardTypeId());
                        dto.setCardTypeName(cardType.getCardTypeName());
                    }
                });

        return dto;
    }
}

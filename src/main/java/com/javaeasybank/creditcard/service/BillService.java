package com.javaeasybank.creditcard.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaeasybank.creditcard.dto.CardBillResponseDto;
import com.javaeasybank.creditcard.entity.CardBill;
import com.javaeasybank.creditcard.entity.CardTransaction;
import com.javaeasybank.creditcard.entity.CreditCard;
import com.javaeasybank.creditcard.enums.BillStatus;
import com.javaeasybank.creditcard.mapper.CardBillMapper;
import com.javaeasybank.creditcard.repository.CardBillRepository;
import com.javaeasybank.creditcard.repository.CardTxnRepository;
import com.javaeasybank.creditcard.repository.CreditCardRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BillService {

	private final CardBillRepository cardBillRepository;
	private final CardBillMapper cardBillMapper;
	private final CreditCardRepository creditCardRepository;
	private final CardTxnRepository cardTransactionRepository;
	
	public Page<CardBillResponseDto> getBills(Pageable pageable) {
		
		return cardBillRepository.findAll(pageable).map(cardBillMapper::toDto);
	}

	public Integer generateBills(){

	int count = 0;
		List<CreditCard> cards = creditCardRepository.findAll();

    for (CreditCard card : cards) {

        List<CardTransaction> txns =
                cardTransactionRepository.findByCardCardId(card.getCardId());

        if (txns.isEmpty()) {
            continue;
        }

        BigDecimal total = txns.stream()
                .map(CardTransaction::getTxnAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CardBill bill = new CardBill();

        bill.setCard(card);

        bill.setBillingMonth(
                YearMonth.now().toString()
        );

        bill.setBillDate(LocalDate.now());

        bill.setDueDate(LocalDate.now().plusDays(14));

        bill.setTotalAmount(total);

        // 最低應繳 10%
        bill.setMinimumPayment(
                total.multiply(BigDecimal.valueOf(0.1))
        );

        bill.setPaidAmount(BigDecimal.ZERO);

        bill.setBillStatus(BillStatus.UNPAID);

        cardBillRepository.save(bill);

        count++;
    }

    return count;
	}

    public Page<CardBillResponseDto> getBillsByCustomerId(String customerId, Pageable pageable) {
        return cardBillRepository.findByCardCustomerCustomerId(customerId, pageable)
                .map(cardBillMapper::toDto);
    }

}

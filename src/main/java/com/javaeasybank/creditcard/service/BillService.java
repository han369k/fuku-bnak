package com.javaeasybank.creditcard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaeasybank.creditcard.dto.CardBillResponseDto;
import com.javaeasybank.creditcard.entity.CardBill;
import com.javaeasybank.creditcard.repository.CardBillRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BillService {

	private final CardBillRepository cardBillRepository;
	
	public List<CardBillResponseDto> findAll() {
		List<CardBill>bills=cardBillRepository.findAll();
		return bills.stream()
        .map(this::toDto)
        .toList();
	}
	
	private CardBillResponseDto toDto(CardBill bill) {
	    CardBillResponseDto dto = new CardBillResponseDto();

	    dto.setBillId(bill.getBillId());
	    dto.setBillingMonth(bill.getBillingMonth());
	    dto.setBillDate(bill.getBillDate());
	    dto.setDueDate(bill.getDueDate());
	    dto.setTotalAmount(bill.getTotalAmount());
	    dto.setMinimumPayment(bill.getMinimumPayment());
	    dto.setPaidAmount(bill.getPaidAmount());
	    dto.setBillStatus(bill.getBillStatus());

	    return dto;
	}
}

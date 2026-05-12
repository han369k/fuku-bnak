package com.javaeasybank.creditcard.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaeasybank.creditcard.dto.CardBillResponseDto;
import com.javaeasybank.creditcard.mapper.CardBillMapper;
import com.javaeasybank.creditcard.repository.CardBillRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BillService {

	private final CardBillRepository cardBillRepository;
	private final CardBillMapper cardBillMapper;
	
	public Page<CardBillResponseDto> getBills(Pageable pageable) {
		
		return cardBillRepository.findAll(pageable).map(cardBillMapper::toDto);
	}
}

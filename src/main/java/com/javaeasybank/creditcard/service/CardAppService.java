package com.javaeasybank.creditcard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaeasybank.common.exception.BusinessException;
import com.javaeasybank.creditcard.dto.CardApplicationResponseDto;
import com.javaeasybank.creditcard.dto.CardApplicationRequest;
import com.javaeasybank.creditcard.entity.CardApplication;
import com.javaeasybank.creditcard.enums.CardApplicationStatus;
import com.javaeasybank.creditcard.mapper.CardApplicationMapper;
import com.javaeasybank.creditcard.repository.CardAppRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class CardAppService {

	private final CardAppRepository cardAppRepository;
	private final CardApplicationMapper cardApplicationMapper;
	
	public List<CardApplicationResponseDto> findAllDto() {
		return cardApplicationMapper.toDtoList(cardAppRepository.findAll());
	}
	public CardApplication findById(Integer id) {
	    return cardAppRepository.findById(id)
	        .orElseThrow(() -> new BusinessException("Credit card application not found."));
	}
	// 查單筆（DTO）
		public CardApplicationResponseDto findDtoById(Integer id) {
			return cardApplicationMapper.toDto(findById(id));
		}
	public void deleteById(Integer id) {
		if (!cardAppRepository.existsById(id)) {
	        throw new BusinessException("Credit card application not found.");
	    }
	    cardAppRepository.deleteById(id);
	}
	//新增申請
	public CardApplicationResponseDto create(CardApplicationRequest requestDto) {
	    CardApplication entity = cardApplicationMapper.toEntity(requestDto);
	    //預設值
	    entity.setStatus(CardApplicationStatus.PENDING);

	    CardApplication saved = cardAppRepository.save(entity);

	    return cardApplicationMapper.toDto(saved);
	}
}

package com.javaeasybank.creditcard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.javaeasybank.creditcard.entity.CardType;
import com.javaeasybank.creditcard.repository.CardTypeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CardTypeService {

	
	private final CardTypeRepository cardTypeRepository;

	public CardType insert(CardType cardType) {
		return cardTypeRepository.save(cardType);
	}
	public CardType update(CardType cardType) {
		return cardTypeRepository.save(cardType);
	}
	public void deleteById(Integer id) {
		cardTypeRepository.deleteById(id);
	}
	public CardType findById(Integer id) {
		return cardTypeRepository.findById(id).orElse(null);
	}
	public List<CardType> findAll() {
		return cardTypeRepository.findAll();
	}
	
	
	
	
	
	
	
}

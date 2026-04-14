package com.javaeasybank.creditcard.service;

import org.springframework.stereotype.Service;

import com.javaeasybank.creditcard.entity.CardApplicationItem;
import com.javaeasybank.creditcard.repository.CardAppItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardAppItemService {

	private final CardAppItemRepository cardAppItemRepository;
	
	public void save(CardApplicationItem cardApplicationItem) {
		cardAppItemRepository.save(cardApplicationItem);
	}
	public void deleteById(Integer id) {
		cardAppItemRepository.deleteById(id);
	}
	public void findById(Integer id) {
		cardAppItemRepository.findById(id);
	}

}

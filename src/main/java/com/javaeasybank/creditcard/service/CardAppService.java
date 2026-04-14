package com.javaeasybank.creditcard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.javaeasybank.creditcard.entity.CardApplication;
import com.javaeasybank.creditcard.repository.CardAppRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CardAppService {

	private final CardAppRepository cardAppRepository;
	
	public List<CardApplication> findAll() {
		return cardAppRepository.findAll();
	}
	public void findById(Integer id) {
		cardAppRepository.findById(id);
	}
	public void save(CardApplication cardApplication) {
		cardAppRepository.save(cardApplication);
	}
	public void deleteById(Integer id) {
		cardAppRepository.deleteById(id);
	}
	
}

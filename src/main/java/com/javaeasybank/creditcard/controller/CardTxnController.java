package com.javaeasybank.creditcard.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaeasybank.creditcard.dto.CardTxnRequestDto;
import com.javaeasybank.creditcard.dto.CardTxnResponseDto;
import com.javaeasybank.creditcard.service.CardTxnService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card-txns")
public class CardTxnController {

	private final CardTxnService cardTxnService;
	
	@PostMapping
    public CardTxnResponseDto create(@RequestBody CardTxnRequestDto dto) {
        return cardTxnService.create(dto);
    }
}

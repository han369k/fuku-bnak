package com.javaeasybank.creditcard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaeasybank.creditcard.service.CreditCardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/cards")
@RequiredArgsConstructor
public class CardAdminController {

    private final CreditCardService cardService;
}

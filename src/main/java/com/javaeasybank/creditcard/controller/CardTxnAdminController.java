package com.javaeasybank.creditcard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaeasybank.creditcard.service.CardTxnService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/card-txns")
@RequiredArgsConstructor
public class CardTxnAdminController {

    private final CardTxnService cardTxnService;



}

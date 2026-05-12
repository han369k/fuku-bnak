package com.javaeasybank.creditcard.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaeasybank.common.dto.response.ApiResponse;
import com.javaeasybank.common.dto.response.PageResponse;
import com.javaeasybank.creditcard.dto.CardBillResponseDto;
import com.javaeasybank.creditcard.service.BillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user/card-bills")
@RequiredArgsConstructor
public class CardBillController {

    private final BillService cardBillService;

    // 查全部帳單
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CardBillResponseDto>>> getBills(Pageable pageable) {
        Page<CardBillResponseDto> page= cardBillService.getBills(pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.of(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements())));
    }


}

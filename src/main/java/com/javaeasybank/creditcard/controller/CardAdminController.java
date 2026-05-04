package com.javaeasybank.creditcard.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaeasybank.creditcard.dto.CreditCardRequestDto;
import com.javaeasybank.creditcard.dto.CreditCardResponseDto;
import com.javaeasybank.creditcard.service.CreditCardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/cards")
@RequiredArgsConstructor
public class CardAdminController {

    private final CreditCardService cardService;

    // 查詢卡列表
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCards() {
        return ResponseEntity.ok(Map.of(
                "message", "Credit cards retrieved successfully",
                "data", cardService.findAll()
        ));
    }

    // 查詢單一卡
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCardById(@PathVariable Integer id) {
        return ResponseEntity.ok(Map.of(
                "message", "Credit card retrieved successfully",
                "data", cardService.findById(id)
        ));
    }

    // 新增卡
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCard(@RequestBody CreditCardRequestDto dto) {
        CreditCardResponseDto created = cardService.create(dto);
        return ResponseEntity.ok(Map.of(
                "message", "Credit card created successfully",
                "data", created
        ));
    }

    // 更新卡
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCard(
            @PathVariable Integer id,
            @RequestBody CreditCardRequestDto dto
    ) {
        CreditCardResponseDto updated = cardService.update(id, dto);
        return ResponseEntity.ok(Map.of(
                "message", "Credit card updated successfully",
                "data", updated
        ));
    }

    // 刪除卡
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCard(@PathVariable Integer id) {
        cardService.deleteById(id);
        return ResponseEntity.ok(Map.of(
                "message", "Credit card deleted successfully"
        ));
    }
}

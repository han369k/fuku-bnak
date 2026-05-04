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

import com.javaeasybank.creditcard.dto.CardTxnRequestDto;
import com.javaeasybank.creditcard.service.CardTxnService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/card-txns")
@RequiredArgsConstructor
public class CardTxnAdminController {

    private final CardTxnService cardTxnService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTransactions() {
        return ResponseEntity.ok(Map.of(
                "message", "Transactions retrieved successfully",
                "data", cardTxnService.findAll()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTransaction(@PathVariable Integer id) {
        return ResponseEntity.ok(Map.of(
                "message", "Transaction retrieved successfully",
                "data", cardTxnService.findById(id)
        ));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTransaction(@RequestBody CardTxnRequestDto dto) {
        return ResponseEntity.ok(Map.of(
                "message", "Transaction created successfully",
                "data", cardTxnService.create(dto)
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTransaction(
            @PathVariable Integer id,
            @RequestBody CardTxnRequestDto dto
    ) {
        return ResponseEntity.ok(Map.of(
                "message", "Transaction updated successfully",
                "data", cardTxnService.update(id, dto)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTransaction(@PathVariable Integer id) {
        cardTxnService.deleteById(id);
        return ResponseEntity.ok(Map.of(
                "message", "Transaction deleted successfully"
        ));
    }
}

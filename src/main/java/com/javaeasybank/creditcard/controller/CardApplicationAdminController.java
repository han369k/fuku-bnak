package com.javaeasybank.creditcard.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaeasybank.creditcard.dto.CardApplicationResponseDto;
import com.javaeasybank.creditcard.enums.CardApplicationStatus;
import com.javaeasybank.creditcard.service.CardAppService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/card-applications")
@RequiredArgsConstructor
public class CardApplicationAdminController {

    private final CardAppService cardAppService;
    // 查全部
    @GetMapping
    public ResponseEntity<List<CardApplicationResponseDto>> getAll() {
        return ResponseEntity.ok(cardAppService.findAll());
    }
    // 查單筆（DTO）
    @GetMapping("/{id}")
    public ResponseEntity<CardApplicationResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(cardAppService.findById(id));
    }
    // 更新狀態
    @PutMapping("/{id}/status")
    public ResponseEntity<CardApplicationResponseDto> updateStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {

        String status = body.get("status");
        CardApplicationResponseDto updated = cardAppService.updateStatus(id, CardApplicationStatus.valueOf(status));

        return ResponseEntity.ok(updated);
    }
    // 刪除申請
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteById(@PathVariable Integer id) {
        cardAppService.deleteById(id);
        return ResponseEntity.ok(Map.of(
                "message", "Card application deleted"
        ));
    }   
}

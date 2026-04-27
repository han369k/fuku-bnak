package com.javaeasybank.creditcard.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.javaeasybank.creditcard.dto.CardTypeRequestDto;
import com.javaeasybank.creditcard.dto.CardTypeResponseDto;
import com.javaeasybank.creditcard.service.CardTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/card-types")
@RequiredArgsConstructor
public class CardTypeAdminController {

    private final CardTypeService cardTypeService;

    // 查詢所有卡別
    @GetMapping
    public ResponseEntity<List<CardTypeResponseDto>> getAll() {
        return ResponseEntity.ok(cardTypeService.findAll());
    }

    // 查詢單一卡別
    @GetMapping("/{id}")
    public ResponseEntity<CardTypeResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(cardTypeService.findById(id));
    }

    // 新增卡別（含圖片）
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @RequestPart("data") CardTypeRequestDto req,
            @RequestPart("mf") MultipartFile mf
    ) throws IOException {

        CardTypeResponseDto card = cardTypeService.createCardType(req, mf);

        return ResponseEntity.ok(Map.of(
                "message", "Card type created",
                "data", card
        ));
    }

    // 更新卡別（圖片可選）
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Integer id,
            @RequestPart("data") CardTypeRequestDto req,
            @RequestPart(value = "mf", required = false) MultipartFile mf
    ) throws IOException {

        CardTypeResponseDto card = cardTypeService.updateCardType(id, req, mf);

        return ResponseEntity.ok(Map.of(
                "message", "Card type updated",
                "data", card
        ));
    }

    // 刪除卡別
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletebyId(@PathVariable Integer id) {

        cardTypeService.deleteById(id);

        return ResponseEntity.ok(Map.of(
                "message", "Card type deleted"
        ));
    }
}

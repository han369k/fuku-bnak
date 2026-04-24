package com.javaeasybank.creditcard.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.javaeasybank.creditcard.dto.CardTypeCreateRequest;
import com.javaeasybank.creditcard.entity.CardType;
import com.javaeasybank.creditcard.service.CardTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/card-types")
@RequiredArgsConstructor
public class CardTypeController {

	private final CardTypeService cardTypeService;

    // 🔹 1. 查全部卡別
    @GetMapping
    public ResponseEntity<List<CardType>> getAll() {
        return ResponseEntity.ok(cardTypeService.findAll());
    }

    // 🔹 2. 查單一卡別
    @GetMapping("/{id}")
    public ResponseEntity<CardType> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(cardTypeService.findById(id));
    }

    // 🔹 3. 新增卡別（含圖片）
    @PostMapping
    public ResponseEntity<?> create(
            @ModelAttribute CardTypeCreateRequest req,
            @RequestParam("mf") MultipartFile mf
    ) throws IOException {

        CardType card = cardTypeService.createCardType(req, mf);

        return ResponseEntity.ok(Map.of(
                "message", "Card type created",
                "data", card
        ));
    }

    // 🔹 4. 更新卡別（圖片可選）
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @ModelAttribute CardTypeCreateRequest req,
            @RequestParam(value = "mf", required = false) MultipartFile mf
    ) throws IOException {

        CardType card = cardTypeService.updateCardType(id, req, mf);

        return ResponseEntity.ok(Map.of(
                "message", "Card type updated",
                "data", card
        ));
    }

    // 🔹 5. 刪除卡別
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        cardTypeService.deleteById(id);

        return ResponseEntity.ok(Map.of(
                "message", "Card type deleted"
        ));
    }
}

package com.javaeasybank.creditcard.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaeasybank.creditcard.entity.CardApplication;
import com.javaeasybank.creditcard.enums.CardApplicationStatus;
import com.javaeasybank.creditcard.service.CardAppService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/card-applications")
@RequiredArgsConstructor
public class CardApplicationController {

	
	private final CardAppService cardAppService;
	
	@PostMapping
    public ResponseEntity<?> apply(@RequestBody CardApplication application) {
        cardAppService.save(application);
        return ResponseEntity.ok(Map.of("message", "Application submitted"));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(cardAppService.findAll());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {

        String status = body.get("status");
        CardApplication app = cardAppService.findById(id);
              

        app.setStatus(CardApplicationStatus.valueOf(status));
        cardAppService.save(app);

        return ResponseEntity.ok(Map.of("message", "updated"));
    }
}

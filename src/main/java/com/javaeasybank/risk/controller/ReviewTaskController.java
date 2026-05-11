package com.javaeasybank.risk.controller;

import com.javaeasybank.common.dto.response.ApiResponse;
import com.javaeasybank.risk.dto.response.ReviewTaskResponse;
import com.javaeasybank.risk.service.ReviewTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/risk/reviewtask")
public class ReviewTaskController {

    final ReviewTaskService reviewTaskService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ReviewTaskResponse>>> getAllReviewTasks(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ReviewTaskResponse> page = reviewTaskService.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(page));
    }
}
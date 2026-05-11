package com.javaeasybank.risk.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javaeasybank.risk.enums.BusinessScene;
import com.javaeasybank.risk.enums.ReviewResult;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Builder
public class ReviewTaskResponse {

    private Long taskId;
    private String businessId;
    private BusinessScene scene;
    private String status;
    private ReviewResult reviewResult;
    private String assignee;
    private String adminComment;
    private Integer priority;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime processedAt;
}

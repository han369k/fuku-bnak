package com.javaeasybank.risk.dto.request;

import com.javaeasybank.risk.core.enums.ReviewResult;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewDecisionRequest {/**
 * 最終決策：APPROVED / REJECTED
 */
@NotNull(message = "reviewResult 不可為空")
private ReviewResult reviewResult;

    /**
     * 審核人備註
     */
    private String adminComment;

}

package com.javaeasybank.risk.dto;

import com.javaeasybank.risk.core.enums.BlacklistType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class BlackListResponse {

    private BlacklistType listType;

    private String listValue;

    private String source;

    private String reason;

    private Boolean status = Boolean.TRUE;

    private LocalDateTime expireAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

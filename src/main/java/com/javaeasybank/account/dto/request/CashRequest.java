package com.javaeasybank.account.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;


/*
* 存、提款請求的 DTO ，用於接收客戶端的存提款資訊。
* 都使用這同一個 DTO 做請求就好
* */
@Data
public class CashRequest {

    // 操作帳號
    @NotBlank
    private String accountNumber;

    // 操作金額
    @NotNull
    private BigDecimal amount;

    // 備註
    private String note;
}

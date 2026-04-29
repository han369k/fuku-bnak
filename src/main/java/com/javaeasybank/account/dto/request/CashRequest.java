package com.javaeasybank.account.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    // 這是專門給 String 用的檢查
    // 同時檢查 1. NotNull 2. NotEmpty 3. NotOnlyBlank
    private String accountNumber;

    // 操作金額
    @NotNull
    @Positive // 確保金額大於零
    private BigDecimal amount;

    // 備註
    private String note;
}

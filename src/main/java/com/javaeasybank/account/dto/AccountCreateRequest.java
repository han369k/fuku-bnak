package com.javaeasybank.account.dto;

import com.javaeasybank.account.enums.AccountType;
import com.javaeasybank.account.enums.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountCreateRequest {
    @NotNull
    private Long customerId;
    
    @NotNull
    private AccountType accountType;
    
    @NotNull
    private Currency currency;

    private String parentAccountNumber;
}

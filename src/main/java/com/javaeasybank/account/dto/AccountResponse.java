package com.javaeasybank.account.dto;

import com.javaeasybank.account.entity.Account;
import com.javaeasybank.account.enums.AccountStatus;
import com.javaeasybank.account.enums.AccountType;
import com.javaeasybank.account.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountResponse {
    private String accountNumber;
    private Long customerId;
    private AccountType accountType;
    private Currency currency;
    private BigDecimal balance;
    private BigDecimal liability;
    private BigDecimal interestRate;
    private AccountStatus status;
    private String parentAccountNumber;
    private LocalDateTime createdAt;
    
    public static AccountResponse fromEntity(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountNumber(account.getAccountNumber());
        response.setCustomerId(account.getCustomerId());
        response.setAccountType(account.getAccountType());
        response.setCurrency(account.getCurrency());
        response.setBalance(account.getBalance());
        response.setLiability(account.getLiability());
        response.setInterestRate(account.getInterestRate());
        response.setStatus(account.getStatus());
        response.setParentAccountNumber(account.getParentAccountNumber());
        response.setCreatedAt(account.getCreatedAt());
        return response;
    }
}

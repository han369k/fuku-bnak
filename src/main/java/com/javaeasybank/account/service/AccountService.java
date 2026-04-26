package com.javaeasybank.account.service;

import com.javaeasybank.account.dto.AccountCreateRequest;
import com.javaeasybank.account.dto.AccountResponse;
import com.javaeasybank.account.entity.Account;
import com.javaeasybank.account.enums.AccountStatus;
import com.javaeasybank.account.enums.AccountType;
import com.javaeasybank.account.enums.Currency;
import com.javaeasybank.account.exception.AccountException;
import com.javaeasybank.account.repository.AccountRepository;
import com.javaeasybank.account.utils.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private static final BigDecimal CHECKING_INITIAL_BALANCE = new BigDecimal("1000");
    private static final BigDecimal CHECKING_INTEREST_RATE = new BigDecimal("0.0015");

    private final AccountRepository accountRepository;

    /**
     * 建立新帳戶。
     * 執行 KYC 驗證、帳戶類型規則驗證，並根據帳戶類型設定初始餘額和利率。
     *
     * @param request 帳戶創建請求。
     * @return 創建成功的帳戶響應。
     * @throws AccountException 如果 KYC 驗證失敗或帳戶類型規則不符。
     */
    @Transactional
    public AccountResponse createAccount(AccountCreateRequest request) {
        // 1. KYC 驗證 (Mock: 假設都通過)
        validateKyc(request.getCustomerId());

        // 2. 帳戶型別持有數規則驗證
        validateAccountTypeRules(request);

        // 3. 建立 Account Entity
        Account account = new Account();
        account.setAccountNumber(AccountNumberGenerator.generate());
        account.setCustomerId(request.getCustomerId());
        account.setAccountType(request.getAccountType());
        account.setCurrency(request.getCurrency());
        account.setStatus(AccountStatus.PENDING); // 預設 PENDING
        
        // 處理子帳戶的父帳戶綁定
        if (request.getAccountType() == AccountType.SUB_ACCOUNT) {
             account.setParentAccountNumber(request.getParentAccountNumber());
        }

        // 4. 活存帳戶強制綁定初始餘額 1,000，並設定固定利率 0.15%
        if (request.getAccountType() == AccountType.CHECKING) {
            account.setBalance(CHECKING_INITIAL_BALANCE.setScale(request.getCurrency().getDecimalPlaces(), RoundingMode.HALF_EVEN));
            account.setInterestRate(CHECKING_INTEREST_RATE);
        } else if (request.getAccountType() == AccountType.SUB_ACCOUNT) {
             // 子帳戶利率比照活存
             account.setInterestRate(CHECKING_INTEREST_RATE);
        }

        // 儲存並回傳
        Account savedAccount = accountRepository.save(account);
        log.info("Successfully created account: {}", savedAccount.getAccountNumber());
        return AccountResponse.fromEntity(savedAccount);
    }

    /**
     * 根據帳號查詢單一帳戶。
     *
     * @param accountNumber 要查詢的帳號。
     * @return 查詢到的帳戶響應。
     * @throws AccountException 如果找不到指定帳號的帳戶。
     */
    @Transactional(readOnly = true)
    public AccountResponse getAccount(String accountNumber) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountException("ACCOUNT_NOT_FOUND", "Account not found: " + accountNumber));
        return AccountResponse.fromEntity(account);
    }

    /**
     * 根據客戶 ID 查詢所有帳戶並進行分頁。
     *
     * @param customerId 客戶 ID。
     * @param pageable   分頁資訊。
     * @return 包含帳戶響應的分頁列表。
     */
    @Transactional(readOnly = true)
    public Page<AccountResponse> getAccountsByCustomerId(Long customerId, Pageable pageable) {
        return accountRepository.findByCustomerId(customerId, pageable)
                .map(AccountResponse::fromEntity);
    }

    /**
     * 根據帳戶狀態篩選帳戶並進行分頁。
     *
     * @param status   要篩選的帳戶狀態。
     * @param pageable 分頁資訊。
     * @return 包含帳戶響應的分頁列表。
     */
    @Transactional(readOnly = true)
    public Page<AccountResponse> getAccountsByStatus(AccountStatus status, Pageable pageable) {
        return accountRepository.findByStatus(status, pageable)
                .map(AccountResponse::fromEntity);
    }

    /**
     * 根據帳戶類型和貨幣篩選帳戶並進行分頁。
     *
     * @param type     要篩選的帳戶類型。
     * @param currency 要篩選的貨幣。
     * @param pageable 分頁資訊。
     * @return 包含帳戶響應的分頁列表。
     */
    @Transactional(readOnly = true)
    public Page<AccountResponse> getAccountsByTypeAndCurrency(AccountType type, Currency currency, Pageable pageable) {
        return accountRepository.findByAccountTypeAndCurrency(type, currency, pageable)
                .map(AccountResponse::fromEntity);
    }

    // ==========================================
    // Private Helper Methods
    // ==========================================

    /**
     * 執行客戶的 KYC 驗證。
     * 目前為模擬通過。
     *
     * @param customerId 客戶 ID。
     */
    private void validateKyc(Long customerId) {
        // TODO: 呼叫 Customer Service 驗證 KYC，目前 Mock 通過
        log.debug("KYC validation passed for customer: {}", customerId);
    }

    /**
     * 驗證帳戶創建請求的帳戶類型規則。
     * 包括活存帳戶的唯一性、子帳戶的貨幣限制、父帳戶要求及所有權驗證。
     *
     * @param request 帳戶創建請求。
     * @throws AccountException 如果帳戶類型規則不符。
     */
    private void validateAccountTypeRules(AccountCreateRequest request) {
        Long customerId = request.getCustomerId();
        AccountType type = request.getAccountType();
        Currency currency = request.getCurrency();

        if (type == AccountType.CHECKING) {
            // CHECKING(活存): 同客戶 + 同 currency 只能有一個
            boolean hasDuplicateChecking = accountRepository.existsByCustomerIdAndAccountTypeAndCurrency(customerId, type, currency);
            
            if (hasDuplicateChecking) {
                throw new AccountException("DUPLICATE_CHECKING_ACCOUNT", "Customer already has a checking account in " + currency);
            }
        } else if (type == AccountType.SUB_ACCOUNT) {
            // SUB_ACCOUNT(子帳戶): 限定台幣，且必須持有正常狀態(ACTIVE)的台幣活存
            if (currency != Currency.TWD) {
                throw new AccountException("SUB_ACCOUNT_CURRENCY_MUST_BE_TWD", "Sub-accounts must be in TWD");
            }
            
            boolean hasActiveTwdChecking = accountRepository.existsByCustomerIdAndAccountTypeAndCurrencyAndStatus(
                    customerId, AccountType.CHECKING, Currency.TWD, AccountStatus.ACTIVE);
                    
            if (!hasActiveTwdChecking) {
                 throw new AccountException("NO_ACTIVE_TWD_CHECKING", "Customer must have an ACTIVE TWD checking account to create a sub-account");
            }
            
            if (request.getParentAccountNumber() == null || request.getParentAccountNumber().isBlank()) {
                throw new AccountException("PARENT_ACCOUNT_REQUIRED", "Sub-accounts must provide a parent account number");
            }
            
            // 子帳戶建立時，新增驗證: parentAccountNumber 對應的帳戶必須存在，且該帳戶的 customerId 必須與 request 的 customerId 一致。
            Account parentAccount = accountRepository.findById(request.getParentAccountNumber())
                    .orElseThrow(() -> new AccountException("PARENT_ACCOUNT_NOT_FOUND", "Parent account not found: " + request.getParentAccountNumber()));
            
            if (!parentAccount.getCustomerId().equals(customerId)) {
                throw new AccountException("PARENT_ACCOUNT_OWNER_MISMATCH", "Parent account does not belong to the same customer");
            }
        }
        // TIME_DEPOSIT(定存) 和 LOAN(貸款): 不檢查重複，可無限開
    }
}

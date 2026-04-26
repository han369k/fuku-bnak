package com.javaeasybank.account.repository;

import com.javaeasybank.account.entity.Account;
import com.javaeasybank.account.enums.AccountStatus;
import com.javaeasybank.account.enums.AccountType;
import com.javaeasybank.account.enums.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    /**
     * 依 customer_id 查客戶名下所有帳戶
     */
    Page<Account> findByCustomerId(Long customerId, Pageable pageable);

    /**
     * 依 status 篩選 (例如撈出所有凍結帳戶)
     */
    Page<Account> findByStatus(AccountStatus status, Pageable pageable);

    /**
     * 依 account_type + currency 篩選
     */
    Page<Account> findByAccountTypeAndCurrency(AccountType accountType, Currency currency, Pageable pageable);
}

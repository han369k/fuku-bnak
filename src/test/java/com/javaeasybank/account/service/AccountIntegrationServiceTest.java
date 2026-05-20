package com.javaeasybank.account.service;

import com.javaeasybank.account.dto.request.LoanInterestRequest;
import com.javaeasybank.account.entity.Account;
import com.javaeasybank.account.entity.TransLog;
import com.javaeasybank.account.enums.AccountStatus;
import com.javaeasybank.account.enums.AccountType;
import com.javaeasybank.account.enums.Currency;
import com.javaeasybank.account.enums.EntryType;
import com.javaeasybank.account.enums.TransactionType;
import com.javaeasybank.account.repository.AccountRepository;
import com.javaeasybank.account.repository.TransLogRepository;
import com.javaeasybank.creditcard.repository.CardBillRepository;
import com.javaeasybank.creditcard.repository.CreditCardRepository;
import com.javaeasybank.customer.repository.CustomerProfileRepository;
import com.javaeasybank.loan.repository.LoanAccountRepository;
import com.javaeasybank.loan.repository.LoanRepaymentRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountIntegrationServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransLogRepository transLogRepository;
    @Mock
    private CustomerProfileRepository customerProfileRepository;
    @Mock
    private CardBillRepository cardBillRepository;
    @Mock
    private CreditCardRepository creditCardRepository;
    @Mock
    private LoanAccountRepository loanAccountRepository;
    @Mock
    private LoanRepaymentRepository loanRepaymentRepository;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private AccountIntegrationService service;

    @Test
    @DisplayName("addLoanInterest 使用 TransLogRepository 儲存交易紀錄")
    void addLoanInterest_savesTransLogWithRepository() {
        Account loanAccount = new Account();
        loanAccount.setAccountNumber("90301123456789");
        loanAccount.setAccountType(AccountType.LOAN);
        loanAccount.setCurrency(Currency.TWD);
        loanAccount.setStatus(AccountStatus.ACTIVE);
        loanAccount.setBalance(BigDecimal.ZERO);
        loanAccount.setLiability(new BigDecimal("1000.00"));

        when(accountRepository.findAllByAccountNumberInForUpdate(List.of("90301123456789")))
                .thenReturn(List.of(loanAccount));

        LoanInterestRequest request = new LoanInterestRequest();
        request.setLoanAccountNumber("90301123456789");
        request.setAmount(new BigDecimal("125.00"));
        request.setNote("測試利息");

        service.addLoanInterest(request);

        ArgumentCaptor<TransLog> captor = ArgumentCaptor.forClass(TransLog.class);
        verify(transLogRepository).save(captor.capture());
        TransLog savedLog = captor.getValue();

        assertEquals("90301123456789", savedLog.getAccountNumber());
        assertEquals(EntryType.DEBIT, savedLog.getEntryType());
        assertEquals(TransactionType.INTEREST, savedLog.getTransactionType());
        assertEquals(new BigDecimal("125.00"), savedLog.getAmount());
        assertEquals(new BigDecimal("1000.00"), savedLog.getBalanceBefore());
        assertEquals(new BigDecimal("1125.00"), savedLog.getBalanceAfter());
    }
}

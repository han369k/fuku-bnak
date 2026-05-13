package com.javaeasybank.loan.scheduler;

import com.javaeasybank.loan.entity.LoanAccount;
import com.javaeasybank.loan.entity.LoanRepayment;
import com.javaeasybank.loan.enums.LoanAccountStatus;
import com.javaeasybank.loan.enums.LoanRepaymentStatus;
import com.javaeasybank.loan.repository.LoanAccountRepository;
import com.javaeasybank.loan.repository.LoanRepaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * 貸款還款排程批次：
 *   每日 01:00 掃描應繳日已過但仍為 SCHEDULED 的期數，標記為 OVERDUE，
 *   並同步將對應 LoanAccount.accountStatus 升級為 OVERDUE。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoanRepaymentScheduler {

    private final LoanRepaymentRepository repaymentRepo;
    private final LoanAccountRepository   loanAccountRepo;

    /**
     * 逾期掃描：每日 01:00 執行
     * cron = 秒 分 時 日 月 週
     */
    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void scanOverdueRepayments() {

        LocalDate today = LocalDate.now();
        log.info("[OverdueScan] 開始掃描 date={}", today);

        // 應繳日 < 今天 且 仍為 SCHEDULED → 逾期
        List<LoanRepayment> overdues = repaymentRepo
                .findByScheduledDateBeforeAndRepaymentStatus(today, LoanRepaymentStatus.SCHEDULED);

        if (overdues.isEmpty()) {
            log.info("[OverdueScan] 無逾期期數");
            return;
        }

        // 批次標記 OVERDUE
        LocalDateTime now = LocalDateTime.now();
        overdues.forEach(rp -> {
            rp.setRepaymentStatus(LoanRepaymentStatus.OVERDUE);
            rp.setUpdateTime(now);
        });
        repaymentRepo.saveAll(overdues);

        // 將涉及的 LoanAccount 狀態升級為 OVERDUE（若目前仍是 ACTIVE）
        Map<String, List<LoanRepayment>> byAccount = overdues.stream()
                .collect(Collectors.groupingBy(LoanRepayment::getAccountId));

        List<LoanAccount> accountsToUpdate = loanAccountRepo
                .findAllById(byAccount.keySet())
                .stream()
                .filter(a -> a.getAccountStatus() == LoanAccountStatus.ACTIVE)
                .collect(Collectors.toList());

        accountsToUpdate.forEach(a -> {
            a.setAccountStatus(LoanAccountStatus.OVERDUE);
            a.setUpdateTime(now);
        });
        loanAccountRepo.saveAll(accountsToUpdate);

        log.info("[OverdueScan] 完成：逾期期數={} 涉及帳戶={}",
                overdues.size(), accountsToUpdate.size());
    }
}

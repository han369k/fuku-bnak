package com.javaeasybank.loan.service;

import com.javaeasybank.loan.entity.LoanAccount;
import com.javaeasybank.loan.entity.LoanRepayment;
import com.javaeasybank.loan.enums.LoanRepaymentStatus;
import com.javaeasybank.loan.repository.LoanRepaymentRepository;
import com.javaeasybank.loan.utils.AmortizationCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/*
 * 負責 LoanRepayment 的建立與查詢。
 * 目前只實作「撥款後預排 N 期」，還款進度回寫留待第四部補齊。
 */
@Slf4j
@Service
@Transactional
public class LoanRepaymentService {

    @Autowired
    private LoanRepaymentRepository repaymentRepo;

    /**
     * 撥款後一次性預排 N 期還款明細，status 全部設為 SCHEDULED。
     * 由 LoanAccountService.createOnDisbursement() 在同一個事務內呼叫。
     *
     * @param account 剛建立完成的 LoanAccount（需含 rate、confirmedPeriod、nextPaymentDate）
     */
    public void createSchedule(LoanAccount account) {

        List<AmortizationCalculator.RepaymentRow> rows = AmortizationCalculator.buildSchedule(
                account.getRemainingPrincipal(),    // 撥款本金
                account.getRate(),                  // 年利率
                account.getConfirmedPeriod(),        // 還款總期數
                account.getNextPaymentDate()         // 第 1 期應繳日（= startDate + 1 個月）
        );

        List<LoanRepayment> repayments = rows.stream().map(row -> {
            LoanRepayment rp = new LoanRepayment();
            rp.setRepaymentId(generateId("RP"));
            rp.setAccountId(account.getAccountId());
            rp.setPeriodIndex(row.periodIndex());
            rp.setScheduledDate(row.scheduledDate());
            rp.setTotalAmount(row.totalAmount());
            rp.setPrincipalPortion(row.principalPortion());
            rp.setInterestPortion(row.interestPortion());
            rp.setRemainingAfter(row.remainingAfter());
            rp.setRepaymentStatus(LoanRepaymentStatus.SCHEDULED);
            rp.setCreateTime(LocalDateTime.now());
            return rp;
        }).collect(Collectors.toList());

        repaymentRepo.saveAll(repayments);
        log.info("[RepaymentSchedule] 預排完成 accountId={} periods={}",
                account.getAccountId(), repayments.size());
    }

    // 產生格式化 ID（前綴 + yyyyMMddHHmmss + 4 位亂數），與其他 Service 同規格
    private String generateId(String prefix) {
        String timeStr      = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomSuffix = String.format("%04d", (int) (Math.random() * 10000));
        return prefix + timeStr + randomSuffix;
    }
}

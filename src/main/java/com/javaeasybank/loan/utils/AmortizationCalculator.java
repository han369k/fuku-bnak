package com.javaeasybank.loan.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 等額本息攤還計算工具（純靜態，無 Spring 依賴，可直接單元測試）
 *
 * 提供兩個入口：
 *   calcMonthlyPayment() - 計算每月應繳金額（元，無條件進位）
 *   buildSchedule()      - 展開每一期的本金 / 利息 / 剩餘本金明細
 */
public class AmortizationCalculator {

    private AmortizationCalculator() {}

    /**
     * 等額本息月付金：M = P × r × (1+r)^n / ((1+r)^n − 1)
     * 若利率為 0（如學生貸款），退化為平均攤還：M = ceil(P / n)
     */
    public static BigDecimal calcMonthlyPayment(BigDecimal principal, BigDecimal annualRate, int periods) {
        if (annualRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(periods), 0, RoundingMode.CEILING);
        }
        BigDecimal r            = annualRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal onePlusR     = BigDecimal.ONE.add(r);
        BigDecimal onePlusRPowN = onePlusR.pow(periods, new MathContext(20, RoundingMode.HALF_UP));
        BigDecimal numerator    = principal.multiply(r).multiply(onePlusRPowN);
        BigDecimal denominator  = onePlusRPowN.subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 0, RoundingMode.CEILING);
    }

    /**
     * 展開完整攤還表：逐期計算本金 / 利息 / 繳完後剩餘本金
     *
     * @param principal        撥款本金
     * @param annualRate       年利率（例如 0.04 代表 4%）
     * @param periods          還款總期數
     * @param firstPaymentDate 第 1 期應繳日（通常為 startDate + 1 個月）
     * @return 長度為 periods 的列表，索引 0 = 第 1 期
     */
    public static List<RepaymentRow> buildSchedule(
            BigDecimal principal,
            BigDecimal annualRate,
            int periods,
            LocalDate firstPaymentDate) {

        BigDecimal monthlyPayment = calcMonthlyPayment(principal, annualRate, periods);
        BigDecimal r = annualRate.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : annualRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        List<RepaymentRow> rows = new ArrayList<>(periods);
        BigDecimal remaining = principal;

        for (int i = 1; i <= periods; i++) {
            BigDecimal interest  = remaining.multiply(r).setScale(0, RoundingMode.HALF_UP);
            BigDecimal principal2;
            BigDecimal payment;

            if (i == periods) {
                // 最後一期：直接清零，消除累積舍入誤差
                principal2 = remaining;
                payment    = remaining.add(interest);
            } else {
                principal2 = monthlyPayment.subtract(interest);
                payment    = monthlyPayment;
            }

            remaining = remaining.subtract(principal2);

            rows.add(new RepaymentRow(
                    i,
                    firstPaymentDate.plusMonths(i - 1),
                    payment,
                    principal2,
                    interest,
                    remaining
            ));
        }
        return rows;
    }

    /** 每一期攤還明細（不可變 record）*/
    public record RepaymentRow(
            int        periodIndex,
            LocalDate  scheduledDate,
            BigDecimal totalAmount,
            BigDecimal principalPortion,
            BigDecimal interestPortion,
            BigDecimal remainingAfter
    ) {}
}

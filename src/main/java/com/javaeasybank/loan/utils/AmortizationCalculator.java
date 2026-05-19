package com.javaeasybank.loan.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// 等額本息（本息平均攤還）計算工具
public class AmortizationCalculator {

    // 工具類，禁止外部實例化
    private AmortizationCalculator() {}

    // 計算每月應繳的固定金額（等額本息月付金）
    public static BigDecimal calcMonthlyPayment(BigDecimal principal, BigDecimal annualRate, int periods) {
        if (annualRate.compareTo(BigDecimal.ZERO) == 0) {
            // 零利率：直接平均攤還，無條件進位
            return principal.divide(BigDecimal.valueOf(periods), 0, RoundingMode.CEILING);
        }
        BigDecimal r            = annualRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal onePlusR     = BigDecimal.ONE.add(r);
        BigDecimal onePlusRPowN = onePlusR.pow(periods, new MathContext(20, RoundingMode.HALF_UP));
        BigDecimal numerator    = principal.multiply(r).multiply(onePlusRPowN);
        BigDecimal denominator  = onePlusRPowN.subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 0, RoundingMode.CEILING);
    }

    // 展開完整攤還表，逐期計算本金 / 利息 / 繳完後剩餘本金
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
            // 本期利息 = 剩餘本金 × 月利率（四捨五入至整數元）
            BigDecimal interest = remaining.multiply(r).setScale(0, RoundingMode.HALF_UP);
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

    // 單期攤還明細（不可變 record）
    public record RepaymentRow(
            int        periodIndex,
            LocalDate  scheduledDate,
            BigDecimal totalAmount,
            BigDecimal principalPortion,
            BigDecimal interestPortion,
            BigDecimal remainingAfter
    ) {}
}

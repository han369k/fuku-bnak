package com.javaeasybank.risk;

import com.javaeasybank.risk.core.enums.Occupation;
import com.javaeasybank.risk.entity.CustomerCreditInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.ThreadLocalRandom;

public class CreditMockUtils {

    // 各年齡段可選的職業池
    private static final Occupation[] YOUNG_OCCUPATIONS = {
            Occupation.STUDENT,
            Occupation.OFFICE_WORKER,
            Occupation.MANUFACTURING,
            Occupation.SERVICE_INDUSTRY,
            Occupation.FREELANCER,
            Occupation.GOVERNMENT_EMPLOYEE
    };

    private static final Occupation[] MID_OCCUPATIONS = {
            Occupation.OFFICE_WORKER,
            Occupation.GOVERNMENT_EMPLOYEE,
            Occupation.MANAGER,
            Occupation.PROFESSIONAL,
            Occupation.MANUFACTURING,
            Occupation.SERVICE_INDUSTRY,
            Occupation.SELF_EMPLOYED,
            Occupation.FREELANCER,
            Occupation.HOUSEWIFE
    };

    private static final Occupation[] SENIOR_OCCUPATIONS = {
            Occupation.MANAGER,
            Occupation.PROFESSIONAL,
            Occupation.GOVERNMENT_EMPLOYEE,
            Occupation.SELF_EMPLOYED,
            Occupation.SERVICE_INDUSTRY,
            Occupation.RETIRED,
            Occupation.HOUSEWIFE,
            Occupation.UNEMPLOYED
    };

    /**
     * 根據 customerId 與生日，產生一筆模擬信用資料
     *
     * @param customerId 客戶ID，必須與 CustomerProfile 一致
     * @param birthday   客戶生日，用於推算年齡與職業分布
     * @return 尚未持久化的 CustomerCreditInfo
     * @throws IllegalArgumentException 若生日為未來日期
     */
    public static CustomerCreditInfo generateMockInfo(String customerId, LocalDate birthday) {
        validateBirthday(birthday);

        int age = Period.between(birthday, LocalDate.now()).getYears();
        Occupation occupation = selectOccupationByAge(age);

        CustomerCreditInfo info = new CustomerCreditInfo();
        info.setCustomerId(customerId);
        info.setOccupation(occupation);
        info.setAnnualIncome(generateIncome(occupation, age));
        info.setOtherBankDebt(generateDebt(info.getAnnualIncome()));
        info.setExternalScore(ThreadLocalRandom.current().nextInt(300, 801));
        info.setHasRealEstate(generateHasRealEstate(age));

        return info;
    }

    private static void validateBirthday(LocalDate birthday) {
        if (birthday == null) {
            throw new IllegalArgumentException("Birthday must not be null");
        }
        if (birthday.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birthday cannot be in the future: " + birthday);
        }
    }

    private static Occupation selectOccupationByAge(int age) {
        var random = ThreadLocalRandom.current();

        if (age < 22) return Occupation.STUDENT;

        Occupation[] pool = age < 35 ? YOUNG_OCCUPATIONS
                : age < 55 ? MID_OCCUPATIONS
                  : SENIOR_OCCUPATIONS;

        return pool[random.nextInt(pool.length)];
    }

    /**
     * 年齡加成邏輯：
     * 每多一歲，在基礎收入上加 seniority * 年資乘數
     * 年資從各職業的「預設起始年齡」開始計算
     */
    private static BigDecimal generateIncome(Occupation occupation, int age) {
        var random = ThreadLocalRandom.current();
        double base;
        double seniorityBonus;

        switch (occupation) {

            // --- 穩定受僱 ---
            case OFFICE_WORKER -> {
                // 一般上班族：40萬~80萬，25歲後每年 +8,000
                base = 400_000 + random.nextInt(400_000);
                seniorityBonus = Math.max(0, age - 25) * 8_000.0;
            }
            case GOVERNMENT_EMPLOYEE -> {
                // 公務員：穩定但天花板低，區間窄，年資加成也較低
                base = 500_000 + random.nextInt(200_000); // 50萬~70萬
                seniorityBonus = Math.max(0, age - 25) * 6_000.0;
            }
            case PROFESSIONAL -> {
                // 醫師/律師/會計師：入行晚，但起薪高
                base = 1_500_000 + random.nextInt(500_000); // 150萬~200萬
                seniorityBonus = Math.max(0, age - 30) * 30_000.0;
            }
            case MANAGER -> {
                // 管理職：需資歷，35歲後加成明顯
                base = 1_000_000 + random.nextInt(500_000); // 100萬~150萬
                seniorityBonus = Math.max(0, age - 35) * 20_000.0;
            }
            case MANUFACTURING -> {
                // 製造業勞工：收入偏低，天花板有限
                base = 350_000 + random.nextInt(150_000); // 35萬~50萬
                seniorityBonus = Math.max(0, age - 22) * 3_000.0;
            }
            case SERVICE_INDUSTRY -> {
                // 服務業：收入偏低且波動大，年資加成幾乎沒有
                base = 300_000 + random.nextInt(200_000); // 30萬~50萬
                seniorityBonus = Math.max(0, age - 22) * 2_000.0;
            }

            // --- 自主經營/自由業 ---
            case SELF_EMPLOYED -> {
                // 自營業者：波動大，但資深者收入可觀
                base = 500_000 + random.nextInt(1_000_000); // 50萬~150萬
                seniorityBonus = Math.max(0, age - 30) * 15_000.0;
            }
            case FREELANCER -> {
                // 自由業：波動最大，年資加成中等
                base = 400_000 + random.nextInt(800_000); // 40萬~120萬
                seniorityBonus = Math.max(0, age - 28) * 10_000.0;
            }

            // --- 非在職狀態 ---
            case STUDENT -> {
                // 學生：打工收入，無年資加成
                base = 50_000 + random.nextInt(100_000); // 5萬~15萬
                seniorityBonus = 0;
            }
            case RETIRED -> {
                // 退休：退休金/勞保，固定且偏低
                base = 200_000 + random.nextInt(200_000); // 20萬~40萬
                seniorityBonus = 0; // 退休後無年資加成
            }
            case HOUSEWIFE -> {
                // 家庭主婦/主夫：無固定收入，偶有兼職
                base = random.nextInt(100_000); // 0~10萬
                seniorityBonus = 0;
            }
            case UNEMPLOYED -> {
                // 待業者：無收入或極低（政府補助）
                base = random.nextInt(50_000); // 0~5萬
                seniorityBonus = 0;
            }
        }

        return BigDecimal.valueOf(base + seniorityBonus)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 負債為年收入的 0.0 ~ 2.0 倍隨機值
     * 上限 2 倍模擬最壞情況（房貸 + 信貸並存）
     */
    private static BigDecimal generateDebt(BigDecimal annualIncome) {
        // 先產生整數倍數避免 double 精度問題：0 ~ 200 代表 0.00 ~ 2.00
        int debtPermille = ThreadLocalRandom.current().nextInt(0, 201); // 0~200
        BigDecimal factor = BigDecimal.valueOf(debtPermille, 2);        // scale=2，即除以100
        return annualIncome.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 有房機率隨年齡提升：
     * < 30 歲：5%，30~45 歲：20%，> 45 歲：40%
     */
    private static boolean generateHasRealEstate(int age) {
        double probability = age < 30 ? 0.05
                : age < 45 ? 0.20
                  : 0.40;
        return ThreadLocalRandom.current().nextDouble() < probability;
    }
}


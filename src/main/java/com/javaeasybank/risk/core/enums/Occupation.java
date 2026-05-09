package com.javaeasybank.risk.core.enums;

import lombok.Getter;

/**
 * 職業類型列舉 - 用於風險評估與隨機資料產生
 */
public enum Occupation {
    // 穩定受僱
    OFFICE_WORKER("上班族", EmploymentStatus.EMPLOYED),
    GOVERNMENT_EMPLOYEE("公務人員", EmploymentStatus.EMPLOYED),
    PROFESSIONAL("專業人士 (醫/律/會)", EmploymentStatus.EMPLOYED),
    MANAGER("管理職", EmploymentStatus.EMPLOYED),
    MANUFACTURING("製造業勞工", EmploymentStatus.EMPLOYED),
    SERVICE_INDUSTRY("服務業人員", EmploymentStatus.EMPLOYED),

    // 自主經營/自由業
    SELF_EMPLOYED("自營業者", EmploymentStatus.EMPLOYED),
    FREELANCER("自由職業者", EmploymentStatus.EMPLOYED),

    // 非在職狀態
    STUDENT("學生", EmploymentStatus.UNEMPLOYED),
    RETIRED("退休人員", EmploymentStatus.UNEMPLOYED),
    HOUSEWIFE("家庭主婦/主夫", EmploymentStatus.UNEMPLOYED),
    UNEMPLOYED("待業者", EmploymentStatus.UNEMPLOYED);

    private final String desc;
    private final EmploymentStatus defaultStatus; // 預設就業狀態

    Occupation(String desc, EmploymentStatus defaultStatus) {
        this.desc = desc;
        this.defaultStatus = defaultStatus;
    }

    public String getDesc() {
        return desc;
    }

    public EmploymentStatus getDefaultStatus() {
        return defaultStatus;
    }
}

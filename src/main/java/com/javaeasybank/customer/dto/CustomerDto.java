package com.javaeasybank.customer.dto;

import com.javaeasybank.risk.core.RiskTarget;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerDto {

    @Getter
    @Setter
    public static
    class CustomerRequest implements RiskTarget {
        private String customerId;
        private String idNumber;
        private String name;
        private LocalDate birthday;
        private String gender;
        private String email;
        private String phone;
        private String address;
        @Override public String getTargetIdentifier() { return idNumber; }
        @Override public BigDecimal getAmount() { return BigDecimal.ZERO; }
    }

    @Getter
    @Setter
    public static class CustomerResponse {
        private String customerId;
        private String cif;
        private String idNumber;
        private String name;
        private LocalDate birthday;
        private String gender;
        private String email;
        private String phone;
        private String address;
        private String status;
    }
}
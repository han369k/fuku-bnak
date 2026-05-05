package com.javaeasybank.customer.dto;

import com.javaeasybank.risk.core.RiskTarget;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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

        @Override
        public Map<String, Object> getRiskMetadata() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("idNumber", idNumber);
            metadata.put("phone", phone);
            metadata.put("email", email);
            metadata.put("address", address);
            return metadata;
        }

        @Override
        public String getTargetIdentifier() {
            return customerId;
        }

        @Override
        public BigDecimal getAmount() {
            return BigDecimal.ZERO;
        }
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
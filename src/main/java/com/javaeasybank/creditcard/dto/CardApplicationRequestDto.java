package com.javaeasybank.creditcard.dto;

import lombok.Data;

@Data
public class CardApplicationRequestDto {
	private Integer customerId;
    private String remark;
}

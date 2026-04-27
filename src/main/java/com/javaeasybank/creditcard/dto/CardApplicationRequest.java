package com.javaeasybank.creditcard.dto;

import lombok.Data;

@Data
public class CardApplicationRequest {
	private Integer customerId;
    private String remark;
}

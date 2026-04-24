package com.javaeasybank.creditcard.dto;

import lombok.Data;

@Data
public class CreateCardApplicationRequest {
	private Integer customerId;
    private String remark;
}

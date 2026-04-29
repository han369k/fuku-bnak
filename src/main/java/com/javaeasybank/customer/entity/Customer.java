package com.javaeasybank.customer.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Customer {

	@Id
	private Integer customerId;
	private String name;
	
}

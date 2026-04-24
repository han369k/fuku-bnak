package com.javaeasybank.customer.entity;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Customer {

	@Id
	private Integer customerId;
	private String name;
	
}

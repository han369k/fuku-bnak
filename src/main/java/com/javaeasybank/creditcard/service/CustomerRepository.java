package com.javaeasybank.creditcard.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaeasybank.customer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}

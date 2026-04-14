package com.javaeasybank.creditcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaeasybank.creditcard.entity.CreditCard;

public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

}

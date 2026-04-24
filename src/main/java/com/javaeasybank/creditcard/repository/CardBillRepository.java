package com.javaeasybank.creditcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaeasybank.creditcard.entity.CardBill;

public interface CardBillRepository extends JpaRepository<CardBill, Integer> {

}

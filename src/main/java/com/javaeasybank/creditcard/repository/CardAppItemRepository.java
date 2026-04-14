package com.javaeasybank.creditcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaeasybank.creditcard.entity.CardApplicationItem;

public interface CardAppItemRepository extends JpaRepository<CardApplicationItem, Integer> {

}

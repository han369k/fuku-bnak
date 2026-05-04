package com.javaeasybank.creditcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaeasybank.creditcard.entity.CardApplication;

public interface CardAppRepository extends JpaRepository<CardApplication, Integer> {

}

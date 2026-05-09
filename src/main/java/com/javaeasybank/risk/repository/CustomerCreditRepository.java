package com.javaeasybank.risk.repository;

import com.javaeasybank.risk.entity.CustomerCreditInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerCreditRepository extends JpaRepository<CustomerCreditInfo, Integer> {
}

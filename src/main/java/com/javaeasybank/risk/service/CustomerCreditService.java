package com.javaeasybank.risk.service;

import com.javaeasybank.risk.repository.CustomerCreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerCreditService {
    private final CustomerCreditRepository ccRepos;
}

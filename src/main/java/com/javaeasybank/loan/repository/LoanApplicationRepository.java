package com.javaeasybank.loan.repository;

import com.javaeasybank.loan.entity.LoanApplication;
import com.javaeasybank.loan.enums.LoanApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, String> {

    // 查全部，按建立時間降序
    List<LoanApplication> findAllByOrderByCreateTimeDesc();

    // 依狀態篩選，按建立時間降序
    List<LoanApplication> findByApplicationStatusInOrderByCreateTimeDesc(List<LoanApplicationStatus> statusList);
}
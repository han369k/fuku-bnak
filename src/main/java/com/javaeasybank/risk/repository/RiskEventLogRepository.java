package com.javaeasybank.risk.repository;

import com.javaeasybank.risk.entity.RiskEventLog;
<<<<<<< HEAD
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
>>>>>>> main

public interface RiskEventLogRepository extends JpaRepository<RiskEventLog, Long>, JpaSpecificationExecutor<RiskEventLog> {

}

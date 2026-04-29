package com.javaeasybank.risk.repository;

import com.javaeasybank.risk.entity.RiskEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RiskEventLogRepository extends JpaRepository<RiskEventLog,Long> {

    List<RiskEventLog> findByEventType(String eventType);

    List<RiskEventLog> findByActionTaken(String actionTaken);

    List<RiskEventLog> findByCreatedAt(LocalDateTime createdAt);
}

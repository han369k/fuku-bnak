package com.javaeasybank.risk.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "RISK_EVENT_LOG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskEventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "target_identifier", nullable = false, length = 100)
    private String targetIdentifier;

    @Column(name = "risk_level", nullable = false, length = 20)
    private String riskLevel;

    @Column(name = "action_taken", nullable = false, length = 50)
    private String actionTaken;

    @Column(name = "trigger_reason", length = 500)
    private String triggerReason;

    @Column(name = "transaction_amount", precision = 18, scale = 4)
    private BigDecimal transactionAmount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
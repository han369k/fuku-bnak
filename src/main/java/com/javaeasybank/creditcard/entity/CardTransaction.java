package com.javaeasybank.creditcard.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.javaeasybank.creditcard.enums.TxnType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class CardTransaction {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer txnId;

	@Column(precision = 15, scale = 2)
    private BigDecimal txnAmount;

    @Enumerated(EnumType.STRING)
    private TxnType txnType;

    private LocalDateTime txnDate;

    @Column(length = 200)
    private String description;
    
    private Integer refTxnId;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private CreditCard card;
    
    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;
    
    @ManyToOne
    @JoinColumn(name = "bill_id")
    private CardBill bill;
    
    //預設交易時間
    @PrePersist
    public void prePersist() {
        this.txnDate = LocalDateTime.now();
    }
    
    
    
    
    
    
    
}

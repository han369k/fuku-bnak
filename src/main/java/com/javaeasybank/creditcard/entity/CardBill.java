package com.javaeasybank.creditcard.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.javaeasybank.creditcard.enums.BillStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CardBill {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billId;

    private String billingMonth;

    private LocalDate billDate;

    private LocalDate dueDate;

    private BigDecimal totalAmount;

    private BigDecimal minimumPayment;

    private BigDecimal paidAmount;

    @Enumerated(EnumType.STRING)
    private BillStatus billStatus;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private CreditCard card;
    //退款沖銷用
    @ManyToOne
    @JoinColumn(name = "ref_txn_id")
    private CardTransaction refTxn;
    
    @OneToMany(mappedBy = "bill")
    private List<CardTransaction> transactions;
    
}

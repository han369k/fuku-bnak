package com.javaeasybank.creditcard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.javaeasybank.creditcard.enums.CardStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardResponseDto {

<<<<<<< HEAD
	private String cardId;
=======
	private Integer cardId;
>>>>>>> main
    private String cardNumber;
    private CardTypeResponseDto cardType;
    private BigDecimal creditLimit;
    private BigDecimal currentBalance;
    private CardStatus status;
    private LocalDate expiryDate;
}

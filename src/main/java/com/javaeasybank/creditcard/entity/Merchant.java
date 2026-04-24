package com.javaeasybank.creditcard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer merchantId;

    private String merchantName;
    @Enumerated(EnumType.STRING)
    private String merchantCategory;
//    channel_id NVARCHAR(50),先拿掉
//    channel_secret NVARCHAR(100),

}

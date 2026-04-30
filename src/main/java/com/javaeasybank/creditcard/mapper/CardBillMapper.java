package com.javaeasybank.creditcard.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.javaeasybank.creditcard.dto.CardBillResponseDto;
import com.javaeasybank.creditcard.entity.CardBill;

@Mapper(componentModel = "spring", config = CentralMapperConfig.class)
public interface CardBillMapper {

    CardBillResponseDto toDto(CardBill bill);

    List<CardBillResponseDto> toDtoList(List<CardBill> bills);
}
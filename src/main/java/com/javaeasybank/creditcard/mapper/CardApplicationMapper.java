package com.javaeasybank.creditcard.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.javaeasybank.creditcard.dto.CardApplicationResponseDto;
import com.javaeasybank.creditcard.dto.CreateCardApplicationRequest;
import com.javaeasybank.creditcard.entity.CardApplication;

@Mapper(componentModel = "spring")
public interface CardApplicationMapper {

	@Mapping(source = "customer.customerId", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    CardApplicationResponseDto toDto(CardApplication app);
	
	@Mapping(source = "customerId", target = "customer.customerId")
	CardApplication toEntity(CreateCardApplicationRequest requestDto);

	List<CardApplicationResponseDto> toDtoList(List<CardApplication> all);
}

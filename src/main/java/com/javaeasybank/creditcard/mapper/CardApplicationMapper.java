package com.javaeasybank.creditcard.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.javaeasybank.creditcard.dto.CardApplicationResponseDto;
import com.javaeasybank.creditcard.dto.CardApplicationRequest;
import com.javaeasybank.creditcard.entity.CardApplication;

@Mapper(componentModel = "spring")
public interface CardApplicationMapper {

	@Mapping(source = "customer.customerId", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    CardApplicationResponseDto toDto(CardApplication app);
	
	
	CardApplication toEntity(CardApplicationRequest requestDto);

	List<CardApplicationResponseDto> toDtoList(List<CardApplication> all);
}

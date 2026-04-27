package com.javaeasybank.creditcard.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.javaeasybank.creditcard.dto.CreditCardRequestDto;
import com.javaeasybank.creditcard.dto.CreditCardResponseDto;
import com.javaeasybank.creditcard.entity.CreditCard;

@Mapper(componentModel = "spring",config = CentralMapperConfig.class)
public interface CreditCardMapper {

    // 新增用
    //不須Mapping，交給Service
    // @Mapping(source = "cardTypeId", target = "cardType.cardTypeId")
    // @Mapping(source = "applicationItemId", target = "applicationItem.applicationItemId")
    CreditCard toEntity(CreditCardRequestDto dto);

    // 查詢用
    //不須Mapping，交給Service
    // @Mapping(source = "cardType.cardTypeName", target = "cardTypeName")
    CreditCardResponseDto toDto(CreditCard entity);

    // 更新用
    //不須Mapping，交給Service
    // @Mapping(source = "cardTypeId", target = "cardType.cardTypeId")
    // @Mapping(source = "applicationItemId", target = "applicationItem.applicationItemId")
    void updateEntityFromDto(CreditCardRequestDto dto, @MappingTarget CreditCard entity);

    List<CreditCardResponseDto> toDtoList(List<CreditCard> list);
}

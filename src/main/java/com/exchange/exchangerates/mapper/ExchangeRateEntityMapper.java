package com.exchange.exchangerates.mapper;

import com.exchange.exchangerates.dto.ExchangeRate;
import com.exchange.exchangerates.dto.ExchangeRateResponse;
import com.exchange.exchangerates.entity.ExchangeRateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", imports = {LocalDate.class, LocalDateTime.class})
public interface ExchangeRateEntityMapper {

    @Mapping(target = "exchangeDate",
            expression = "java(LocalDate.parse(LocalDate.parse(dto.getExchangeDate(), DateTimeFormatter.ofPattern(\"dd.MM.yyyy\"))" +
                         ".format(DateTimeFormatter.ofPattern(\"yyyy-MM-dd\"))))")
    @Mapping(target = "lastUpdated", expression = "java(LocalDateTime.now())")
    @Mapping(target = "currencyId", source = "id")
    @Mapping(target = "id", ignore = true)
    ExchangeRateEntity mapToEntity(ExchangeRate dto);

    List<ExchangeRateEntity> mapToListEntity(List<ExchangeRate> rates);

    ExchangeRateResponse mapEntityToResponse(ExchangeRateEntity entity);

    List<ExchangeRateResponse> mapListEntityToResponse(List<ExchangeRateEntity> entityList);

    List<ExchangeRateResponse> mapListToResponse(List<ExchangeRate> body);
}

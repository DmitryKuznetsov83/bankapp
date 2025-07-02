package ru.yandex.practicum.bank.exchange.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.bank.exchange.dto.CurrencyRateDto;
import ru.yandex.practicum.bank.exchange.model.CurrencyRate;

@Mapper
public interface RateMapper {

    RateMapper INSTANCE = Mappers.getMapper(RateMapper.class);

    CurrencyRateDto toCurrencyRateDto(CurrencyRate currencyRate);

    CurrencyRate toCurrencyRate(CurrencyRateDto currencyRateDto);

}
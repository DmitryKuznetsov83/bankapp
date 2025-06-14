package ru.yandex.practicum.bank.exchange.service;

import ru.yandex.practicum.bank.exchange.dto.CurrencyRateDto;

import java.util.List;

public interface RateService {

    List<CurrencyRateDto> getCurrentRates();

    void registerRates(List<CurrencyRateDto> rates);

}

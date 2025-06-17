package ru.yandex.practicum.bank.exchange.service;

import ru.yandex.practicum.bank.exchange.dto.CurrencyRateDto;
import ru.yandex.practicum.bank.exchange.dto.RelativeExchangeRateDto;
import ru.yandex.practicum.bank.exchange.enums.Currency;

import java.util.List;

public interface RateService {

    List<CurrencyRateDto> getCurrentRates();

    void registerRates(List<CurrencyRateDto> rates);

    RelativeExchangeRateDto getRelativeExchangeRate(Currency fromCurrency, Currency toCurrency);
}


package ru.yandex.practicum.bank.exchange.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.bank.exchange.dto.CurrencyRateDto;
import ru.yandex.practicum.bank.exchange.dto.CurrencyRateView;
import ru.yandex.practicum.bank.exchange.dto.RelativeExchangeRateDto;
import ru.yandex.practicum.bank.exchange.enums.Currency;
import ru.yandex.practicum.bank.exchange.service.RateService;

import java.util.List;

@RestController
@RequestMapping("/rates")
@Validated
public class CurrencyRateController {

    private final RateService rateService;

    @Autowired
    public CurrencyRateController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping
    public List<CurrencyRateView> getCurrentRates() {
        return rateService.getCurrentRates()
                .stream()
                .map(dto -> CurrencyRateView.builder()
                        .currencyTitle(dto.getCurrency().getTitle())
                        .currencyName(dto.getCurrency().name())
                        .rate(dto.getRate())
                        .build()).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerRates(@RequestBody @Valid List<CurrencyRateDto> ratesDtos) {
        rateService.registerRates(ratesDtos);
    }

    @GetMapping("/relative")
    public RelativeExchangeRateDto getRelativeCurrentRate(@RequestParam Currency fromCurrency, @RequestParam Currency toCurrency) {
        return rateService.getRelativeExchangeRate(fromCurrency, toCurrency);
    }

}

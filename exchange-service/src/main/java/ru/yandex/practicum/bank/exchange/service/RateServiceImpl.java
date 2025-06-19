package ru.yandex.practicum.bank.exchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.bank.exchange.dto.CurrencyRateDto;
import ru.yandex.practicum.bank.exchange.dto.RelativeExchangeRateDto;
import ru.yandex.practicum.bank.exchange.enums.Currency;
import ru.yandex.practicum.bank.exchange.mapper.RateMapper;
import ru.yandex.practicum.bank.exchange.model.CurrencyRate;
import ru.yandex.practicum.bank.exchange.repository.RateJpaReposirory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class RateServiceImpl implements RateService {

    private final RateJpaReposirory rateJpaReposirory;

    @Autowired
    public RateServiceImpl(RateJpaReposirory rateJpaReposirory) {
        this.rateJpaReposirory = rateJpaReposirory;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CurrencyRateDto> getCurrentRates() {
        return rateJpaReposirory
                .findLatestRates()
                .stream()
                .map(RateMapper.INSTANCE::toCurrencyRateDto)
                .toList();
    }

    @Override
    @Transactional
    public void registerRates(List<CurrencyRateDto> rateDtos) {
        List<CurrencyRate> rateModels = rateDtos
                .stream()
                .map(RateMapper.INSTANCE::toCurrencyRate)
                .toList();
        rateJpaReposirory.saveAll(rateModels);
    }

    @Override
    @Transactional(readOnly = true)
    public RelativeExchangeRateDto getRelativeExchangeRate(Currency fromCurrency, Currency toCurrency) {
        List<CurrencyRate> latestRates = rateJpaReposirory.findLatestRates();

        Optional<BigDecimal> fromCurrencyRate = latestRates.stream()
                .filter(e -> fromCurrency.equals(e.getCurrency()))
                .findFirst()  // Более предсказуемо, чем findAny()
                .map(CurrencyRate::getRate);

        Optional<BigDecimal> toCurrencyRate = latestRates.stream()
                .filter(e -> toCurrency.equals(e.getCurrency()))
                .findFirst()
                .map(CurrencyRate::getRate);

        BigDecimal relativeRate = fromCurrencyRate.get().divide(toCurrencyRate.get(), 6, RoundingMode.UP);

        return RelativeExchangeRateDto.builder()
                .fromCurrency(fromCurrency)
                .toCurrency(toCurrency)
                .rate(relativeRate)
                .build();
    }

}

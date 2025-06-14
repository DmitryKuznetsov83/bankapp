package ru.yandex.practicum.bank.exchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.bank.exchange.dto.CurrencyRateDto;
import ru.yandex.practicum.bank.exchange.mapper.RateMapper;
import ru.yandex.practicum.bank.exchange.model.CurrencyRate;
import ru.yandex.practicum.bank.exchange.repository.RateJpaReposirory;

import java.util.List;

@Service
public class RateServiceImpl implements RateService {

    private final RateJpaReposirory rateJpaReposirory;

    @Autowired
    public RateServiceImpl(RateJpaReposirory rateJpaReposirory) {
        this.rateJpaReposirory = rateJpaReposirory;
    }

    @Override
    public List<CurrencyRateDto> getCurrentRates() {
        return rateJpaReposirory
                .findLatestRatesJpql()
                .stream()
                .map(RateMapper.INSTANCE::toCurrencyRateDto)
                .toList();
    }

    @Override
    public void registerRates(List<CurrencyRateDto> rateDtos) {
        List<CurrencyRate> rateModels = rateDtos
                .stream()
                .map(RateMapper.INSTANCE::toCurrencyRate)
                .toList();
        rateJpaReposirory.saveAll(rateModels);
    }
}

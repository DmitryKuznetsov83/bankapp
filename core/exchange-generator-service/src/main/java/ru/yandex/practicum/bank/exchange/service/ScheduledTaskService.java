package ru.yandex.practicum.bank.exchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bank.exchange.config.ExchangeGeneratorConfig;
import ru.yandex.practicum.bank.exchange.dto.CurrencyRateDto;
import ru.yandex.practicum.bank.exchange.enums.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@EnableScheduling
public class ScheduledTaskService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ExchangeGeneratorConfig exchangeGeneratorConfig;

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskService.class);

    @Autowired
    public ScheduledTaskService(ExchangeGeneratorConfig exchangeGeneratorConfig) {
        this.exchangeGeneratorConfig = exchangeGeneratorConfig;
    }

    @Scheduled(fixedRate = 1000)
    public void executeEverySecond() {
        List<CurrencyRateDto> rates = new ArrayList<>();
        Arrays.stream(Currency.values()).forEach(currency -> {
            Optional<BigDecimal> maybeRate = generateRate(currency);
            if (maybeRate.isPresent()) {
                rates.add(new CurrencyRateDto(currency, maybeRate.get()));
            }
        });


        try {
            restTemplate
                    .postForEntity("http://bankapp-exchange-service:8080/rates", rates, Void.class);
        } catch (HttpClientErrorException e) {
            logger.warn("Error while sending rate request" + e.getResponseBodyAsString(), e);
        } catch (Throwable e) {
            logger.warn("Error while sending rate request" + e.getMessage(), e);
        }

    }

    private Optional<BigDecimal> generateRate(Currency currency) {
        if (exchangeGeneratorConfig.getMainCurrency().equals(currency)) {
            return Optional.of(new BigDecimal("1.00"));
        }

        ExchangeGeneratorConfig.Range range = exchangeGeneratorConfig.getRates().get(currency);

        if (range == null) {
            return Optional.empty();
        }

        BigDecimal min = range.getMin();
        BigDecimal max = range.getMax();

        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Min must be less than or equal to max");
        }

        int scale = 0;
        Random random = new Random();
        BigDecimal diff = max.subtract(min);
        BigDecimal randomFactor = BigDecimal.valueOf(random.nextDouble());
        BigDecimal scaledRandom = diff.multiply(randomFactor).setScale(scale, RoundingMode.UP);
        BigDecimal result = min.add(scaledRandom);

        return Optional.of(result);
    }

}

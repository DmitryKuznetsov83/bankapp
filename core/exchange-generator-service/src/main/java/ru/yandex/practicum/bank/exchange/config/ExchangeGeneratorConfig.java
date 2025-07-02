package ru.yandex.practicum.bank.exchange.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.bank.exchange.enums.Currency;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "exchange")
@Data
public class ExchangeGeneratorConfig {

    private Currency mainCurrency;
    private Map<Currency, Range> rates = new HashMap<>();

    @Data
    public static class Range {
        BigDecimal min;
        BigDecimal max;
    }

}

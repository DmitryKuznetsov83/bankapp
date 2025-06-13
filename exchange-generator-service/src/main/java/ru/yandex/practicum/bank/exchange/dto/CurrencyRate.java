package ru.yandex.practicum.bank.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.bank.exchange.enums.Currency;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRate {

    private Currency currencyCode;
    private BigDecimal rate;

}

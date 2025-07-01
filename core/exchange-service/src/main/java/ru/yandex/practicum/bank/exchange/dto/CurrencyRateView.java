package ru.yandex.practicum.bank.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRateView {

    private String currencyTitle;
    private String currencyName;
    private BigDecimal rate;

}

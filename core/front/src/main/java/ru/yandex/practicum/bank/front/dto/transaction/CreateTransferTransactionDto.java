package ru.yandex.practicum.bank.front.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.bank.front.enums.Currency;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransferTransactionDto {

    private String fromLogin;
    private String toLogin;
    private Currency fromCurrency;
    private Currency toCurrency;
    private BigDecimal fromSum;

}

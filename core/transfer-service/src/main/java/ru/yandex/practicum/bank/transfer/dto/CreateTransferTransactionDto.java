package ru.yandex.practicum.bank.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.bank.transfer.enums.Currency;

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

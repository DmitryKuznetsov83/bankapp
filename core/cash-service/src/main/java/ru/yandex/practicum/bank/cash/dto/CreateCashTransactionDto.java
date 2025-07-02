package ru.yandex.practicum.bank.cash.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.bank.cash.enums.CashTransactionType;
import ru.yandex.practicum.bank.cash.enums.Currency;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCashTransactionDto {

    @NotNull
    private String userLogin;

    @NotNull
    private Currency currency;

    @NotNull
    private CashTransactionType type;

    @NotNull
    private BigDecimal sum;

}
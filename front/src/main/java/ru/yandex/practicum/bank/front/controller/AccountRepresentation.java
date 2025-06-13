package ru.yandex.practicum.bank.front.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.bank.front.enums.AccountState;
import ru.yandex.practicum.bank.front.enums.Currency;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRepresentation {

    private Currency currency;
    private AccountState state;
    private BigDecimal balance;

}

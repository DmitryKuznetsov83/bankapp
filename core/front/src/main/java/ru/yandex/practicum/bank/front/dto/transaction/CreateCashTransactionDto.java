package ru.yandex.practicum.bank.front.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.bank.front.enums.CashTransactionType;
import ru.yandex.practicum.bank.front.enums.Currency;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCashTransactionDto {

    private String userLogin;
    private Currency currency;
    private CashTransactionType type;
    private BigDecimal sum;

}
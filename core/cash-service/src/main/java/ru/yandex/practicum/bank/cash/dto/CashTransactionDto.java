package ru.yandex.practicum.bank.cash.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.bank.cash.enums.CashTransactionType;
import ru.yandex.practicum.bank.cash.enums.Currency;
import ru.yandex.practicum.bank.cash.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashTransactionDto {

    private UUID id;
    private TransactionStatus status;
    private String comment;
    private Instant createdAt;
    private Instant updatedAt;
    private String userLogin;
    private Currency currency;
    private CashTransactionType type;
    private BigDecimal sum;

}

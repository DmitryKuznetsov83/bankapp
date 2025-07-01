package ru.yandex.practicum.bank.blocker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.bank.blocker.enums.Currency;
import ru.yandex.practicum.bank.blocker.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferTransactionDto {

    private UUID id;
    private TransactionStatus status;
    private String comment;
    private Instant createdAt;
    private Instant updatedAt;
    private String fromLogin;
    private String toLogin;
    private Currency fromCurrency;
    private Currency toCurrency;
    private BigDecimal fromSum;
    private BigDecimal toSum;

}

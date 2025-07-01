package ru.yandex.practicum.bank.user.service;

import ru.yandex.practicum.bank.user.dto.transaction.CashTransactionDto;
import ru.yandex.practicum.bank.user.dto.transaction.TransferTransactionDto;

public interface TransactionService {

    void validateTransaction(CashTransactionDto cashTransactionDto);

    void validateTransaction(TransferTransactionDto transferTransactionDto);

}

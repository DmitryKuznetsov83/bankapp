package ru.yandex.practicum.bank.user.service;

import ru.yandex.practicum.bank.user.dto.transaction.CashTransactionDto;
import ru.yandex.practicum.bank.user.dto.transaction.TransferTransactionDto;

public interface TransactionService {

    void processTransaction(CashTransactionDto cashTransactionDto);

    void processTransaction(TransferTransactionDto transferTransactionDto);

}

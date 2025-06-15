package ru.yandex.practicum.bank.user.service;

import ru.yandex.practicum.bank.user.dto.transaction.CashTransactionDto;

public interface TransactionService {

    void processTransaction(CashTransactionDto cashTransactionDto);

}

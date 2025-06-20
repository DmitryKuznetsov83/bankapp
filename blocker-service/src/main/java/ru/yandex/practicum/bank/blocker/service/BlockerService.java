package ru.yandex.practicum.bank.blocker.service;

import ru.yandex.practicum.bank.blocker.dto.CashTransactionDto;
import ru.yandex.practicum.bank.blocker.dto.TransferTransactionDto;

public interface BlockerService {

    Boolean validateCashTransaction(CashTransactionDto cashTransactionDto);

    Boolean validateTransferTransaction(TransferTransactionDto transferTransactionDto);

}

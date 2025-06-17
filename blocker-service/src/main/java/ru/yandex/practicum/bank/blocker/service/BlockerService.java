package ru.yandex.practicum.bank.blocker.service;

import ru.yandex.practicum.bank.blocker.dto.CashTransactionDto;
import ru.yandex.practicum.bank.blocker.dto.TransferTransactionDto;

public interface BlockerService {

    Boolean blockCashTransaction(CashTransactionDto cashTransactionDto);

    Boolean blockTransferTransaction(TransferTransactionDto transferTransactionDto);

}

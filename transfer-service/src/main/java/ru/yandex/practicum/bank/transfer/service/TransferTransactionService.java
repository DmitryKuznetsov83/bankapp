package ru.yandex.practicum.bank.transfer.service;

import ru.yandex.practicum.bank.transfer.dto.CreateTransferTransactionDto;
import ru.yandex.practicum.bank.transfer.dto.TransferTransactionDto;
import ru.yandex.practicum.bank.transfer.model.TransferTransaction;

import java.util.List;

public interface TransferTransactionService {

    List<TransferTransactionDto> getTransferTransactions(String login);

    TransferTransactionDto createTransferTransaction(CreateTransferTransactionDto createTransferTransactionDto);
}

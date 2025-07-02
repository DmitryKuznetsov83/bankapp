package ru.yandex.practicum.bank.cash.service;

import ru.yandex.practicum.bank.cash.dto.CashTransactionDto;
import ru.yandex.practicum.bank.cash.dto.CreateCashTransactionDto;

import java.util.List;

public interface CashTransactionService {

    List<CashTransactionDto> getCashTransactions(String login);

    CashTransactionDto createCashTransaction(CreateCashTransactionDto createCashTransactionDto);

}

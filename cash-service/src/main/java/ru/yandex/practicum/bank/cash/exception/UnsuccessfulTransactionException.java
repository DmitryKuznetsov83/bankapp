package ru.yandex.practicum.bank.cash.exception;

import ru.yandex.practicum.bank.cash.dto.CashTransactionDto;


public class UnsuccessfulTransactionException extends RuntimeException {

    public UnsuccessfulTransactionException(String reason) {
        super(reason);
    }

}

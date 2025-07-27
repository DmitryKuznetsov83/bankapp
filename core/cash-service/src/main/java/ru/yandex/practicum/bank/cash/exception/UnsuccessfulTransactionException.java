package ru.yandex.practicum.bank.cash.exception;


public class UnsuccessfulTransactionException extends RuntimeException {

    public UnsuccessfulTransactionException(String reason) {
        super(reason);
    }

}

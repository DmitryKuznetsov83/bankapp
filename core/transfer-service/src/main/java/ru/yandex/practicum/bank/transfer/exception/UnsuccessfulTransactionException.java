package ru.yandex.practicum.bank.transfer.exception;

public class UnsuccessfulTransactionException extends RuntimeException {

    public UnsuccessfulTransactionException(String reason) {
        super(reason);
    }

}

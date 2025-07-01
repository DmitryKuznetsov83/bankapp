package ru.yandex.practicum.bank.user.exception.account;

public class SelfTransferInTheSameCurrencyException extends RuntimeException {

    public SelfTransferInTheSameCurrencyException() {
        super("Некорректная транзакция. Нельзя делать перевод себе в той же валюте");
    }

}

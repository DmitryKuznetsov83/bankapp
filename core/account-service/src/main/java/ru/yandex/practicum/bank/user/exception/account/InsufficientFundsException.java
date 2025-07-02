package ru.yandex.practicum.bank.user.exception.account;

import ru.yandex.practicum.bank.user.enums.Currency;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String login, Currency currency) {
        super("Недостаточно средств на счету пользователя '" + login + "' в " + currency);
    }

}

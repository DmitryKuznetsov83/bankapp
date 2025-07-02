package ru.yandex.practicum.bank.user.exception.account;

import ru.yandex.practicum.bank.user.enums.Currency;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String login, Currency currency, boolean exists) {
        super("Счет пользователя '" + login + "' в " + currency + (exists ? " не активен" : " не найден"));
    }

}

package ru.yandex.practicum.bank.user.exception.account;

import ru.yandex.practicum.bank.user.enums.Currency;

import java.util.Set;
import java.util.stream.Collectors;

public class AccountClosingException extends RuntimeException {

    public AccountClosingException(Set<Currency> currencySet) {
        super("Невозможно закрыть счет в " + currencySet.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ")));
    }

}

package ru.yandex.practicum.bank.user.exception;

public class UserAccountNotFoundException extends RuntimeException {

    public UserAccountNotFoundException(String login) {
        super("Аккаунт с логином '" + login + "' не найден");
    }

}

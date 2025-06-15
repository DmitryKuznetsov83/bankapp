package ru.yandex.practicum.bank.user.exception.user;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String login) {
        super("Пользователь с логином '" + login + "' не найден");
    }

}

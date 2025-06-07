package ru.yandex.practicum.bank.user.exception;

public class LoginAlreadyUsedException extends RuntimeException {

    public LoginAlreadyUsedException(String login) {
        super("Логин '" + login + "' уже использовался");
    }

}

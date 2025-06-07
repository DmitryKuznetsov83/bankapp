package ru.yandex.practicum.bank.user.exception;

public class PasswordIsSameAsPreviousException extends RuntimeException {

    public PasswordIsSameAsPreviousException(String login) {
        super("Пароль совпадает с предыдущим");
    }

}

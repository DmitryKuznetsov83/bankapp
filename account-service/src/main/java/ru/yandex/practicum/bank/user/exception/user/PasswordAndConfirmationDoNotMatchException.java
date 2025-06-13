package ru.yandex.practicum.bank.user.exception.user;

public class PasswordAndConfirmationDoNotMatchException extends RuntimeException {

    public PasswordAndConfirmationDoNotMatchException(String login) {
        super("Пароль и подтверждение на совпадают");
    }

}

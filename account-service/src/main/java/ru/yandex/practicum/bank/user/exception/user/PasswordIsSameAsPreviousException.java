package ru.yandex.practicum.bank.user.exception.user;

public class PasswordIsSameAsPreviousException extends RuntimeException {

    public PasswordIsSameAsPreviousException() {
        super("Пароль совпадает с предыдущим");
    }

}

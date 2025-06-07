package ru.yandex.practicum.bank.user.exception;

public class UserNotIsOfLegalAgeException extends RuntimeException {

    public UserNotIsOfLegalAgeException() {
        super("Пользователь не достиг совершеннолетия");
    }

}

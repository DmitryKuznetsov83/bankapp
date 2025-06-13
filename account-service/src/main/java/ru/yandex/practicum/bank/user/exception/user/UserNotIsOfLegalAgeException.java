package ru.yandex.practicum.bank.user.exception.user;

public class UserNotIsOfLegalAgeException extends RuntimeException {

    public UserNotIsOfLegalAgeException() {
        super("Пользователь не достиг совершеннолетия");
    }

}

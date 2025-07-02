package ru.yandex.practicum.bank.transfer.enums;

import lombok.Getter;

@Getter
public enum Currency {
    RUB("Рубли"),
    USD("Доллары"),
    CNY("Юани");

    private final String title;

    Currency(String title) {
        this.title = title;
    }

}

package ru.yandex.practicum.bank.front.enums;

public enum Currency {
    RUB("Рубли"),
    USD("Доллары"),
    CNY("Юани");

    private final String title;

    Currency(String currencyName) {
        this.title = currencyName;
    }

    public String getTitle() {
        return title;
    }
}

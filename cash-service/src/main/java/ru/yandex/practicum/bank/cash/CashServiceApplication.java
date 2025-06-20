package ru.yandex.practicum.bank.cash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.bank.cash",
        "ru.yandex.practicum.bank.common"
})
@EntityScan(basePackages = {
        "ru.yandex.practicum.bank.cash",
        "ru.yandex.practicum.bank.notification"
})
@EnableJpaRepositories(basePackages = {
        "ru.yandex.practicum.bank.cash",
        "ru.yandex.practicum.bank.notification"
})
public class CashServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CashServiceApplication.class, args);
    }

}

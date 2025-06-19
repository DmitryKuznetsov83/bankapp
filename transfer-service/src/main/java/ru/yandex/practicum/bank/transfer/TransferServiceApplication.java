package ru.yandex.practicum.bank.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
        "ru.yandex.practicum.bank.transfer",
        "ru.yandex.practicum.bank.notification"
})
@EnableJpaRepositories(basePackages = {
        "ru.yandex.practicum.bank.transfer",
        "ru.yandex.practicum.bank.notification"
})
public class TransferServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransferServiceApplication.class, args);
    }

}

package ru.yandex.practicum.bank.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.bank.front",
        "ru.yandex.practicum.bank.common"
})
public class FrontServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontServiceApplication.class, args);
    }

}

package ru.yandex.practicum.bank.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.bank.exchange",
        "ru.yandex.practicum.bank.common"
})
@EnableConfigurationProperties
public class ExchangeGeneratorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeGeneratorServiceApplication.class, args);
    }

}

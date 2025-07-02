package ru.yandex.practicum.bank.front.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "backend.api")
@Data
public class BackendProperties {
    String url = "http://localhost:8080";
}

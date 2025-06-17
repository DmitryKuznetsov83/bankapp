package ru.yandex.practicum.bank.blocker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "blocker")
@Data
public class BlockerConfig {

    private BigDecimal minThreshold;
    private BigDecimal maxThreshold;

}

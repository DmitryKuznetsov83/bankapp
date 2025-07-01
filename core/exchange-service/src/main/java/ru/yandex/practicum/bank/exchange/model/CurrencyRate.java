package ru.yandex.practicum.bank.exchange.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.bank.exchange.enums.Currency;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "currency_rate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyRate {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @CreationTimestamp
    private Instant timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", length = 3)
    private Currency currency;

    private BigDecimal rate;

    public CurrencyRate(Currency currency, BigDecimal rate) {
        this.currency = currency;
        this.rate = rate;
    }
}

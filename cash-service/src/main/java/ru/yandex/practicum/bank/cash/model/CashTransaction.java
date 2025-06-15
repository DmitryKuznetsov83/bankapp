package ru.yandex.practicum.bank.cash.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.bank.cash.enums.CashTransactionType;
import ru.yandex.practicum.bank.cash.enums.Currency;
import ru.yandex.practicum.bank.cash.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cash_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashTransaction {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 7)
    private TransactionStatus status;

    private String comment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    String userLogin;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", length = 3)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 8)
    private CashTransactionType type;

    private BigDecimal sum;

}

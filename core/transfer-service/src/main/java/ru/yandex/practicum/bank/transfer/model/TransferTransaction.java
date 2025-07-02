package ru.yandex.practicum.bank.transfer.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.bank.transfer.enums.Currency;
import ru.yandex.practicum.bank.transfer.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transfer_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferTransaction {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 7)
    private TransactionStatus status;

    private String comment;

    String fromLogin;

    String toLogin;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_currency", length = 3)
    private Currency fromCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_currency", length = 3)
    private Currency toCurrency;

    private BigDecimal fromSum;

    private BigDecimal toSum;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

}

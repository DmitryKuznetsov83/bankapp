package ru.yandex.practicum.bank.notification.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.bank.notification.enums.NotificationLevel;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @CreationTimestamp
    private Instant createdAt;

    private String userLogin;

    @Enumerated(EnumType.STRING)
    private NotificationLevel level;

    private String message;

}

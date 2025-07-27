package ru.yandex.practicum.bank.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.bank.notification.model.Notification;

import java.util.UUID;

public interface NotificationJpaRepository extends JpaRepository<Notification, UUID> {
}

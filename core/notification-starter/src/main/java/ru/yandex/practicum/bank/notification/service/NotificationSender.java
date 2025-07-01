package ru.yandex.practicum.bank.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.bank.notification.enums.NotificationLevel;
import ru.yandex.practicum.bank.notification.model.Notification;
import ru.yandex.practicum.bank.notification.repository.NotificationJpaRepository;

@Service
public class NotificationSender {

    private final NotificationJpaRepository notificationJpaRepository;

    public NotificationSender(NotificationJpaRepository notificationJpaRepository) {
        this.notificationJpaRepository = notificationJpaRepository;
    }

    @Transactional
    public void send(String userLogin, NotificationLevel level, String message) {
        Notification notification = Notification.builder()
                .userLogin(userLogin)
                .level(level)
                .message(message)
                .build();
        notificationJpaRepository.save(notification);
    }

}

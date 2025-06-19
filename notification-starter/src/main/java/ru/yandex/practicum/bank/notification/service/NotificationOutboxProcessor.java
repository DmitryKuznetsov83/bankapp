package ru.yandex.practicum.bank.notification.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.bank.notification.dto.NotificationDto;
import ru.yandex.practicum.bank.notification.mapper.NotificationMapper;
import ru.yandex.practicum.bank.notification.model.Notification;
import ru.yandex.practicum.bank.notification.repository.NotificationJpaRepository;

import java.util.List;

@Service
public class NotificationOutboxProcessor {

    private final RestClient restClient = RestClient.create();

    private final NotificationJpaRepository notificationJpaRepository;

    public NotificationOutboxProcessor(NotificationJpaRepository notificationJpaRepository) {
        this.notificationJpaRepository = notificationJpaRepository;
    }

    @Scheduled(fixedDelayString = "PT1s")
    public void process() {
        List<Notification> notifications = notificationJpaRepository.findAll();
        List<NotificationDto> notificationsDto = notifications.stream().map(NotificationMapper.INSTANCE::toNotificationDto).toList();

        restClient
                .post()
                .uri("http://localhost:8088/notifications")
                .body(notificationsDto)
                .retrieve()
                .toBodilessEntity();

        notificationJpaRepository.deleteAll(notifications);

    }

}

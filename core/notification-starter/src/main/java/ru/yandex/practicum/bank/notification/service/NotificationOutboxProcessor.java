package ru.yandex.practicum.bank.notification.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bank.notification.dto.NotificationDto;
import ru.yandex.practicum.bank.notification.mapper.NotificationMapper;
import ru.yandex.practicum.bank.notification.model.Notification;
import ru.yandex.practicum.bank.notification.repository.NotificationJpaRepository;

import java.util.List;

@Service
public class NotificationOutboxProcessor {

    private final RestTemplate restTemplate = new RestTemplate();
    private final NotificationJpaRepository notificationJpaRepository;

    public NotificationOutboxProcessor(NotificationJpaRepository notificationJpaRepository) {
        this.notificationJpaRepository = notificationJpaRepository;
    }

    @Scheduled(fixedDelayString = "PT1s")
    public void process() {
        List<Notification> notifications = notificationJpaRepository.findAll();
        List<NotificationDto> notificationsDto = notifications.stream().map(NotificationMapper.INSTANCE::toNotificationDto).toList();

        if (!notificationsDto.isEmpty()) {
            restTemplate
                    .postForEntity("http://bankapp-notification-service:8080/notifications", notificationsDto, Void.class);
            notificationJpaRepository.deleteAll(notifications);
        }
    }

}

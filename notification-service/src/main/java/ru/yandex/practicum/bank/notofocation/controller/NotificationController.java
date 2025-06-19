package ru.yandex.practicum.bank.notofocation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.bank.notofocation.dto.NotificationDto;

import java.util.List;

import static ru.yandex.practicum.bank.notofocation.enums.NotificationLevel.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @PostMapping
    public void sendNotification(@RequestBody List<NotificationDto> notificationDtos) {
        notificationDtos.forEach(notificationDto -> {

            logger.info("Пользователь {} получает сообщение {} уровня {}",
                    notificationDto.getUserLogin(),
                    notificationDto.getMessage(),
                    notificationDto.getLevel());
        });
    }
}

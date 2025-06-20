package ru.yandex.practicum.bank.notification.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bank.notification.repository.NotificationJpaRepository;
import ru.yandex.practicum.bank.notification.service.NotificationOutboxProcessor;
import ru.yandex.practicum.bank.notification.service.NotificationSender;

@Configuration
@EnableScheduling
@ConditionalOnClass(NotificationOutboxProcessor.class)
public class NotificationStarterAutoConfiguration {

    @Bean
    public NotificationOutboxProcessor notificationOutboxProcessor(RestTemplate restTemplate,
                                                                   NotificationJpaRepository notificationJpaRepository) {
        return new NotificationOutboxProcessor(restTemplate, notificationJpaRepository);
    }

    @Bean
    public NotificationSender notificationSender(NotificationJpaRepository notificationJpaRepository) {
        return new NotificationSender(notificationJpaRepository);
    }
}

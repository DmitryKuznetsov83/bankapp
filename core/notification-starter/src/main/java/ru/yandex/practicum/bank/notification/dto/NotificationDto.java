package ru.yandex.practicum.bank.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.bank.notification.enums.NotificationLevel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private String userLogin;
    private NotificationLevel level;
    private String message;

}

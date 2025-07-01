package ru.yandex.practicum.bank.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.bank.notification.enums.NotificationLevel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    @NotNull
    @NotBlank
    private String userLogin;

    @NonNull
    private NotificationLevel level;

    @NonNull
    @NotBlank
    private String message;

}

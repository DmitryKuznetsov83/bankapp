package ru.yandex.practicum.bank.notification.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.bank.notification.dto.NotificationDto;
import ru.yandex.practicum.bank.notification.model.Notification;

@Mapper
public interface NotificationMapper {

    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    NotificationDto toNotificationDto(Notification notification);

}

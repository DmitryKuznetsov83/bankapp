package ru.yandex.practicum.bank.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.bank.user.dto.user.ShortUserDto;
import ru.yandex.practicum.bank.user.dto.user.UserDto;
import ru.yandex.practicum.bank.user.model.User;


@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(UserDto userDto);

    UserDto toUserDto(User user);

    ShortUserDto toShortUserDto(User user);

}

package ru.yandex.practicum.bank.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.bank.user.dto.CreateUserDto;
import ru.yandex.practicum.bank.user.dto.UserDto;
import ru.yandex.practicum.bank.user.model.User;


@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "passwordHash", source = "password")
    User toUser(CreateUserDto createUserDto);

    UserDto toUserDto(User user);

}

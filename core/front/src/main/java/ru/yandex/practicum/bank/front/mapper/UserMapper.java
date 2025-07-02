package ru.yandex.practicum.bank.front.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.bank.front.dto.user.CreateUserForm;
import ru.yandex.practicum.bank.front.dto.user.UserDto;
import ru.yandex.practicum.bank.front.security.AppUserDetails;


@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    AppUserDetails toAppUser(UserDto userDto);

    UserDto toUserDto(CreateUserForm createUserForm);

}

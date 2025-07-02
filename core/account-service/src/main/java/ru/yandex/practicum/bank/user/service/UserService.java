package ru.yandex.practicum.bank.user.service;

import ru.yandex.practicum.bank.user.dto.user.*;

import java.util.List;

public interface UserService {

    UserDto creatUser(UserDto userDto);

    UserDto updateUser(String login, UpdateUserDto updateUserDto);

    void updateUserPassword(String login, UpdateUserPasswordDto updateUserPasswordDto);

    UserDto getUser(String login);

    List<ShortUserDto> getUsers();

}

package ru.yandex.practicum.bank.user.service;

import ru.yandex.practicum.bank.user.dto.CreateUserDto;
import ru.yandex.practicum.bank.user.dto.UpdateUserDto;
import ru.yandex.practicum.bank.user.dto.UpdateUserPasswordDto;
import ru.yandex.practicum.bank.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto creatUser(CreateUserDto createUserDto);

    UserDto updateUser(String login, UpdateUserDto updateUserDto);

    void updateUserPassword(String login, UpdateUserPasswordDto updateUserPasswordDto);

    void deleteUser(String login);

    UserDto getUser(String login);

    List<String> getUsers();

}

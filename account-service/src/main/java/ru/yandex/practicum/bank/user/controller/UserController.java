package ru.yandex.practicum.bank.user.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.bank.common.dto.ApiErrorDto;
import ru.yandex.practicum.bank.user.dto.user.*;
import ru.yandex.practicum.bank.user.exception.user.*;
import ru.yandex.practicum.bank.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto creatUser(@RequestBody @Valid UserDto userDto) {
        return userService.creatUser(userDto);
    }

    @PutMapping("/{login}")
    public UserDto updateUser(@PathVariable String login, @RequestBody  @Valid UpdateUserDto updateUserDto) {
        return userService.updateUser(login, updateUserDto);
    }

    @PutMapping("/{login}/password")
    public void updateUserPassword(@PathVariable String login, @RequestBody  @Valid UpdateUserPasswordDto updateUserPasswordDto) {
        userService.updateUserPassword(login, updateUserPasswordDto);
    }

    @GetMapping("/{login}")
    public UserDto getUser(@PathVariable String login) {
        return userService.getUser(login);
    }

    @GetMapping
    public List<ShortUserDto> getUsers() {
        return userService.getUsers();
    }

}

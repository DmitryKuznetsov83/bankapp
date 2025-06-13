package ru.yandex.practicum.bank.user.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.bank.user.dto.*;
import ru.yandex.practicum.bank.user.dto.user.CreateUserDto;
import ru.yandex.practicum.bank.user.dto.user.UpdateUserDto;
import ru.yandex.practicum.bank.user.dto.user.UpdateUserPasswordDto;
import ru.yandex.practicum.bank.user.dto.user.UserDto;
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
    public UserDto creatUser(@RequestBody @Valid CreateUserDto createUserDto) {
        return userService.creatUser(createUserDto);
    }

    @PutMapping("/{login}")
    public UserDto updateUser(@PathVariable String login, @RequestBody  @Valid UpdateUserDto updateUserDto) {
        return userService.updateUser(login, updateUserDto);
    }

    @PutMapping("/{login}/password")
    public void updateUserPassword(@PathVariable String login, @RequestBody  @Valid UpdateUserPasswordDto updateUserPasswordDto) {
        userService.updateUserPassword(login, updateUserPasswordDto);
    }

    @DeleteMapping("/{login}")
    public void deleteUser(@PathVariable String login) {
        userService.deleteUser(login);
    }

    @GetMapping("/{login}")
    public UserDto getUser(@PathVariable String login) {
        return userService.getUser(login);
    }

    @GetMapping
    public List<String> getUsers() {
        return userService.getUsers();
    }


    // EXCEPTIONS
    @ExceptionHandler({UserNotIsOfLegalAgeException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorDto handleUserNotIsOfLegalAgeException(final UserNotIsOfLegalAgeException exception) {
        return GlobalExceptionHandler.getApiError(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({LoginAlreadyUsedException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorDto handleLoginAlreadyUsedException(final LoginAlreadyUsedException exception) {
        return GlobalExceptionHandler.getApiError(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({PasswordAndConfirmationDoNotMatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDto handlePasswordAndConfirmationDoNotMatchException(final PasswordAndConfirmationDoNotMatchException exception) {
        return GlobalExceptionHandler.getApiError(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PasswordIsSameAsPreviousException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorDto handlePasswordIsSameIsPreviousException(final PasswordIsSameAsPreviousException exception) {
        return GlobalExceptionHandler.getApiError(exception, HttpStatus.CONFLICT);
    }

}

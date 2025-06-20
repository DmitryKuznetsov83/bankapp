package ru.yandex.practicum.bank.user.controller;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.bank.common.dto.ApiErrorDto;
import ru.yandex.practicum.bank.user.exception.account.AccountClosingException;
import ru.yandex.practicum.bank.user.exception.account.AccountNotFoundException;
import ru.yandex.practicum.bank.user.exception.account.InsufficientFundsException;
import ru.yandex.practicum.bank.user.exception.account.SelfTransferInTheSameCurrencyException;
import ru.yandex.practicum.bank.user.exception.user.LoginAlreadyUsedException;
import ru.yandex.practicum.bank.user.exception.user.UserNotFoundException;
import ru.yandex.practicum.bank.user.exception.user.UserNotIsOfLegalAgeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorDto handleUserNotFoundException(final UserNotFoundException exception) {
        return GlobalExceptionHandler.getApiError(exception, HttpStatus.NOT_FOUND);
    }

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

    @ExceptionHandler({AccountNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorDto handleAccountNotFoundException(final AccountNotFoundException exception) {
        return GlobalExceptionHandler.getApiError(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AccountClosingException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorDto handleAccountClosingException(final AccountClosingException exception) {
        return GlobalExceptionHandler.getApiError(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDto handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        return getApiError(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDto handleHttpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        return getApiError(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SelfTransferInTheSameCurrencyException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorDto handleSelfTransferInTheSameCurrencyException(final SelfTransferInTheSameCurrencyException exception) {
        return getApiError(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({InsufficientFundsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorDto handleInsufficientFundsException(final InsufficientFundsException exception) {
        return GlobalExceptionHandler.getApiError(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDto handleValidationException(final ValidationException exception) {
        return getApiError(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorDto handleThrowable(final Throwable exception) {
        return getApiError(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ApiErrorDto getApiError(Throwable exception, HttpStatus httpStatus) {
        return new ApiErrorDto(exception, httpStatus);
    }

}

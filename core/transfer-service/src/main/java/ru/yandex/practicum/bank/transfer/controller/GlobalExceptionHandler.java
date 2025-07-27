package ru.yandex.practicum.bank.transfer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.bank.common.dto.ApiErrorDto;
import ru.yandex.practicum.bank.transfer.exception.UnsuccessfulTransactionException;


@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler({UnsuccessfulTransactionException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorDto handleUnsuccessfulTransactionException(final UnsuccessfulTransactionException exception) {
        return getApiError(exception, HttpStatus.CONFLICT);
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

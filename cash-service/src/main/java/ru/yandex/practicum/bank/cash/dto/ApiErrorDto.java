package ru.yandex.practicum.bank.cash.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorDto {

    private String exception;
    private String message;
    private String status;
    private String timestamp;

    public ApiErrorDto(Throwable exception, HttpStatus status) {
        this.exception = exception.getClass().getSimpleName();
        this.message = exception.getMessage();
        this.status = status.toString();
        this.timestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
    }
}

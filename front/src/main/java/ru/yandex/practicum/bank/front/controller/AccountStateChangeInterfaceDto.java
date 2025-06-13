package ru.yandex.practicum.bank.front.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountStateChangeInterfaceDto {
    private String currency;
    private boolean active;
}

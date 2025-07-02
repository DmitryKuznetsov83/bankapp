package ru.yandex.practicum.bank.front.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountStateChangeForm {
    private String currency;
    private boolean active;
}

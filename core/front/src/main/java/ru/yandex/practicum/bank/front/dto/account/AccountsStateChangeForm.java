package ru.yandex.practicum.bank.front.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountsStateChangeForm {
    private List<AccountStateChangeForm> accounts;
}

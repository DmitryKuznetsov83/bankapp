package ru.yandex.practicum.bank.front.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.bank.front.dto.account.AccountsChangeRequestDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountStateChangeForm {
//    private List<AccountStateChangeDto> accounts;
    private List<AccountStateChangeInterfaceDto> accounts;
}

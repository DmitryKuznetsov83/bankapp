package ru.yandex.practicum.bank.user.service;

import ru.yandex.practicum.bank.user.dto.account.AccountsChangeRequestDto;
import ru.yandex.practicum.bank.user.dto.account.AccountDto;

import java.util.List;

public interface AccountService {

    List<AccountDto> getAccounts(String login);

    List<AccountDto> changeAccountsState(String login, List<AccountsChangeRequestDto> accountsChangeRequestDtoList);

}

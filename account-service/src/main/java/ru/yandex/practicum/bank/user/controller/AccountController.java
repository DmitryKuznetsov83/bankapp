package ru.yandex.practicum.bank.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.bank.user.dto.ApiErrorDto;
import ru.yandex.practicum.bank.user.dto.account.AccountsChangeRequestDto;
import ru.yandex.practicum.bank.user.dto.account.AccountDto;
import ru.yandex.practicum.bank.user.exception.account.AccountClosingException;
import ru.yandex.practicum.bank.user.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@Validated
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{login}")
    public List<AccountDto> getAccounts(@PathVariable String login) {
        return accountService.getAccounts(login);
    }

    @PostMapping("/{login}")
    public List<AccountDto> changeAccountsState(@PathVariable String login,
                                          @RequestBody List<AccountsChangeRequestDto> accountsChangeRequestDtoList) {
        return accountService.changeAccountsState(login, accountsChangeRequestDtoList);
    }

    // EXCEPTIONS
    @ExceptionHandler({AccountClosingException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorDto handleAccountClosingException(final AccountClosingException exception) {
        return GlobalExceptionHandler.getApiError(exception, HttpStatus.CONFLICT);
    }

}

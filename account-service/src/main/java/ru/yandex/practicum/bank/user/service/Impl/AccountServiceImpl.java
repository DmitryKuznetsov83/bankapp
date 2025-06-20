package ru.yandex.practicum.bank.user.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.bank.user.dto.account.AccountsChangeRequestDto;
import ru.yandex.practicum.bank.user.dto.account.AccountDto;
import ru.yandex.practicum.bank.user.enums.AccountOperation;
import ru.yandex.practicum.bank.user.enums.AccountState;
import ru.yandex.practicum.bank.user.enums.AccountStateChangeRequest;
import ru.yandex.practicum.bank.user.enums.Currency;
import ru.yandex.practicum.bank.user.exception.account.AccountClosingException;
import ru.yandex.practicum.bank.user.exception.user.UserNotFoundException;
import ru.yandex.practicum.bank.user.mapper.AccountMapper;
import ru.yandex.practicum.bank.user.model.Account;
import ru.yandex.practicum.bank.user.model.User;
import ru.yandex.practicum.bank.user.repository.AccountJpaRepository;
import ru.yandex.practicum.bank.user.repository.UserJpaRepository;
import ru.yandex.practicum.bank.user.service.AccountService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    static final BigDecimal ZERO = new BigDecimal("0.00");

    private final AccountJpaRepository accountJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Autowired
    public AccountServiceImpl(AccountJpaRepository accountJpaRepository, UserJpaRepository userJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAccounts(String login) {
        User user = userJpaRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
        List<Account> accounts = accountJpaRepository.findByOwner(user);
        return accounts.stream()
                .map(AccountMapper.INSTANCE::toAccountDto)
                .toList();
    }

    @Override
    @Transactional
    public List<AccountDto> changeAccountsState(String login, List<AccountsChangeRequestDto> accountsChangeRequestDtoList) {
        User user = userJpaRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
        List<Account> accounts = accountJpaRepository.findByOwner(user);

        Map<Currency, AccountOperationResolver> asd = accountsChangeRequestDtoList
                .stream()
                .collect(Collectors.toMap(
                        AccountsChangeRequestDto::getCurrency,
                        e -> new AccountOperationResolver(e.getAccountChangeRequest())
                ));

        for (Account account : accounts) {
            asd.computeIfPresent(account.getCurrency(), (c,q) -> {
                q.accountState = account.getState();
                q.account = account;
                q.accountBalance = account.getBalance();
                return q;
            } );
        }

        asd.values().forEach(AccountOperationResolver::calculateAccountOperation);

        Set<Currency> errors = asd.entrySet().stream()
                .filter(e -> AccountOperation.ERROR.equals(e.getValue().accountOperation))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        if (!errors.isEmpty()) {
            throw new AccountClosingException(errors);
        }

        List<Account> accountsToUpdate = new ArrayList<>();
        for (Map.Entry<Currency, AccountOperationResolver> entry : asd.entrySet() ) {
            if (entry.getValue().accountOperation == AccountOperation.CREATE) {
                Account newAccount = Account.builder()
                        .owner(user)
                        .currency(entry.getKey())
                        .state(AccountState.ACTIVE)
                        .balance(ZERO)
                        .build();
                accountsToUpdate.add(newAccount);
            } else if (entry.getValue().accountOperation == AccountOperation.REOPEN) {
                Account accountToReopen = entry.getValue().account;
                accountToReopen.setState(AccountState.ACTIVE);
                accountsToUpdate.add(accountToReopen);
            } else if (entry.getValue().accountOperation == AccountOperation.CLOSE) {
                Account accountToReopen = entry.getValue().account;
                accountToReopen.setState(AccountState.INACTIVE);
                accountsToUpdate.add(accountToReopen);
            }
        }

        accountJpaRepository.saveAll(accountsToUpdate);

        List<Account> accountsToReturn = accountJpaRepository.findByOwner(user);
        return accountsToReturn.stream()
                .map(AccountMapper.INSTANCE::toAccountDto)
                .toList();

    }

//    AccountChangeRequest	CurrentState	          AccountOperation
//    Open	                X	                      Create
//    Open	                ActiveWithZeroBalance	  -
//    Open	                ActiveWithNonZeroBalance  -
//    Open	                INACTIVE	              Reopen
//    Close	                X	                      -
//    Close	                ActiveWithZeroBalance	  Close
//    Close	                ActiveWithNonZeroBalance  Error
//    Close	                INACTIVE	              -

    private static class AccountOperationResolver {

        AccountStateChangeRequest accountStateChangeRequest;
        AccountState accountState;
        Account account;
        BigDecimal accountBalance;
        AccountOperation accountOperation;

        public AccountOperationResolver(AccountStateChangeRequest accountStateChangeRequest) {
            this.accountStateChangeRequest = accountStateChangeRequest;
        }

        public void calculateAccountOperation() {
            if (AccountStateChangeRequest.OPEN.equals(accountStateChangeRequest)) {
                if (accountState == null) {
                    accountOperation = AccountOperation.CREATE;
                } else if (AccountState.INACTIVE.equals(accountState)) {
                    accountOperation = AccountOperation.REOPEN;
                }
            } else if (AccountStateChangeRequest.CLOSE.equals(accountStateChangeRequest)) {
                if (AccountState.ACTIVE.equals(accountState)) {
                    if (ZERO.compareTo(accountBalance) == 0) {
                        accountOperation = AccountOperation.CLOSE;
                    } else {
                        accountOperation = AccountOperation.ERROR;
                    }
                }
            }
        }
    }

}

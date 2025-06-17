package ru.yandex.practicum.bank.user.service.Impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.bank.user.dto.transaction.CashTransactionDto;
import ru.yandex.practicum.bank.user.dto.transaction.TransferTransactionDto;
import ru.yandex.practicum.bank.user.enums.AccountState;
import ru.yandex.practicum.bank.user.enums.CashTransactionType;
import ru.yandex.practicum.bank.user.enums.Currency;
import ru.yandex.practicum.bank.user.exception.account.AccountNotFoundException;
import ru.yandex.practicum.bank.user.exception.account.InsufficientFundsException;
import ru.yandex.practicum.bank.user.exception.account.SelfTransferInTheSameCurrencyException;
import ru.yandex.practicum.bank.user.exception.user.UserNotFoundException;
import ru.yandex.practicum.bank.user.model.Account;
import ru.yandex.practicum.bank.user.model.User;
import ru.yandex.practicum.bank.user.repository.AccountJpaRepository;
import ru.yandex.practicum.bank.user.repository.UserJpaRepository;
import ru.yandex.practicum.bank.user.service.TransactionService;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountJpaRepository accountJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public TransactionServiceImpl(AccountJpaRepository accountJpaRepository, UserJpaRepository userJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    @Transactional
    public void processTransaction(CashTransactionDto cashTransactionDto) {
        String login = cashTransactionDto.getUserLogin();
        Currency currency = cashTransactionDto.getCurrency();
        BigDecimal sum = cashTransactionDto.getSum();
        CashTransactionType transactionType = cashTransactionDto.getType();

        User user = userJpaRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
        Account account = accountJpaRepository
                .findByOwnerAndCurrency(user, currency)
                .orElseThrow(() -> new AccountNotFoundException(login, currency, false));
        if (AccountState.INACTIVE.equals(account.getState())) {
            throw new AccountNotFoundException(login, currency, true);
        }

        BigDecimal newBalance = account.getBalance();
        if (CashTransactionType.CASH_IN.equals(transactionType)) {
            newBalance = newBalance.add(sum);
        } else if (CashTransactionType.CASH_OUT.equals(transactionType)) {
            newBalance = newBalance.subtract(sum);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientFundsException(login, currency);
            }
        }

        account.setBalance(newBalance);
        accountJpaRepository.save(account);

    }

    @Override
    public void processTransaction(TransferTransactionDto transferTransactionDto) {

        String fromLogin = transferTransactionDto.getFromLogin();
        String toLogin = transferTransactionDto.getToLogin();
        Currency fromCurrency = transferTransactionDto.getFromCurrency();
        Currency toCurrency = transferTransactionDto.getToCurrency();
        BigDecimal fromSum = transferTransactionDto.getFromSum();
        BigDecimal toSum = transferTransactionDto.getToSum();

        if (fromLogin.equals(toLogin) && fromCurrency.equals(toCurrency)) {
            throw new SelfTransferInTheSameCurrencyException();
        }

        User fromUser = userJpaRepository.findByLogin(fromLogin)
                .orElseThrow(() -> new UserNotFoundException(fromLogin));
        User toUser = userJpaRepository.findByLogin(toLogin)
                .orElseThrow(() -> new UserNotFoundException(toLogin));


        Account fromAccount = accountJpaRepository
                .findByOwnerAndCurrency(fromUser, fromCurrency)
                .orElseThrow(() -> new AccountNotFoundException(fromLogin, fromCurrency, false));
        if (AccountState.INACTIVE.equals(fromAccount.getState())) {
            throw new AccountNotFoundException(fromLogin, fromCurrency, true);
        }

        Account toAccount = accountJpaRepository
                .findByOwnerAndCurrency(toUser, toCurrency)
                .orElseThrow(() -> new AccountNotFoundException(toLogin, toCurrency, false));
        if (AccountState.INACTIVE.equals(toAccount.getState())) {
            throw new AccountNotFoundException(toLogin, toCurrency, true);
        }

        BigDecimal fromNewBalance = fromAccount.getBalance();
        fromNewBalance = fromNewBalance.subtract(fromSum);
        if (fromNewBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException(fromLogin, fromCurrency);
        }

        BigDecimal toNewBalance = toAccount.getBalance();
        toNewBalance = toNewBalance.add(toSum);

        fromAccount.setBalance(fromNewBalance);
        toAccount.setBalance(toNewBalance);
        accountJpaRepository.saveAll(Set.of(fromAccount, toAccount));

    }
}

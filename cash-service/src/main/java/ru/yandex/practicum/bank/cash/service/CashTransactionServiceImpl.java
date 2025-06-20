package ru.yandex.practicum.bank.cash.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bank.cash.dto.CashTransactionDto;
import ru.yandex.practicum.bank.cash.dto.CreateCashTransactionDto;
import ru.yandex.practicum.bank.cash.enums.TransactionStatus;
import ru.yandex.practicum.bank.cash.mapper.CashTransactionMapper;
import ru.yandex.practicum.bank.cash.model.CashTransaction;
import ru.yandex.practicum.bank.cash.repository.CashTransactionJpaRepository;
import ru.yandex.practicum.bank.common.dto.ApiErrorDto;
import ru.yandex.practicum.bank.notification.service.NotificationSender;

import java.util.List;

import static ru.yandex.practicum.bank.cash.enums.TransactionStatus.*;
import static ru.yandex.practicum.bank.notification.enums.NotificationLevel.*;

@Service
public class CashTransactionServiceImpl implements CashTransactionService {

    private static final Logger log = LoggerFactory.getLogger(CashTransactionServiceImpl.class);

    private final RestTemplate internalRestTemplate;
    private final CashTransactionJpaRepository cashTransactionJpaRepository;
    private final NotificationSender notificationSender;

    public CashTransactionServiceImpl(RestTemplate internalRestTemplate,
                                      CashTransactionJpaRepository cashTransactionJpaRepository,
                                      NotificationSender notificationSender) {
        this.internalRestTemplate = internalRestTemplate;
        this.cashTransactionJpaRepository = cashTransactionJpaRepository;
        this.notificationSender = notificationSender;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CashTransactionDto> getCashTransactions(String login) {
        return cashTransactionJpaRepository
                .findByUserLogin(login)
                .stream()
                .map(CashTransactionMapper.INSTANCE::toCashTransactionDto)
                .toList();
    }

    @Override
    @Transactional
    public CashTransactionDto createCashTransaction(CreateCashTransactionDto createCashTransactionDto) {
        CashTransaction transaction = CashTransactionMapper.INSTANCE.toCashTransaction(createCashTransactionDto);
        transaction = updateTransactionStatus(transaction, PENDING, null);
        transaction = checkBlocking(transaction);
        if (PENDING.equals(transaction.getStatus())) {
            transaction = checkBalance(transaction);
        }
        return CashTransactionMapper.INSTANCE.toCashTransactionDto(transaction);
    }

    private CashTransaction checkBlocking(CashTransaction transaction) {
        try {
            CashTransactionDto cashTransactionDto = CashTransactionMapper.INSTANCE.toCashTransactionDto(transaction);
            Boolean blocked = internalRestTemplate
                    .postForObject("lb://blocker-service/blockers/cash-transactions/validate", cashTransactionDto, Boolean.class);
            if (Boolean.TRUE.equals(blocked)) {
                String reason = "Транзакция заблокирована как подозрительная";
                transaction = updateTransactionStatus(transaction, FAILED, reason);
                notificationSender.send(transaction.getUserLogin(), WARNING, reason);
            }
        } catch (Throwable e) {
            log.warn("Сервис блокировок недоступен", e);
        }
        return transaction;
    }

    private CashTransaction checkBalance(CashTransaction transaction) {
        try {
            CashTransactionDto cashTransactionDto = CashTransactionMapper.INSTANCE.toCashTransactionDto(transaction);
            internalRestTemplate
                    .postForObject("lb://account-service/transactions/cash-transactions/validate", cashTransactionDto, Void.class);
            transaction = updateTransactionStatus(transaction, SUCCESS, null);
            notificationSender.send(transaction.getUserLogin(), INFO, "Успешное снятие средств");
        } catch (HttpClientErrorException e) {
            String reason = e.getResponseBodyAs(ApiErrorDto.class).getMessage();
            transaction = updateTransactionStatus(transaction, FAILED, reason);
            notificationSender.send(transaction.getUserLogin(), WARNING, reason);
        } catch (Exception e) {
            String reason = "Сервис валидации транзакций недоступен";
            log.warn(reason, e);
            transaction = updateTransactionStatus(transaction, FAILED, reason);
            notificationSender.send(transaction.getUserLogin(), WARNING, reason);
        }
        return transaction;
    }

    private CashTransaction updateTransactionStatus(CashTransaction transaction, TransactionStatus status, String comment) {
        transaction.setStatus(status);
        if (comment != null) {
            transaction.setComment(comment);
        }
        return cashTransactionJpaRepository.save(transaction);
    }

}

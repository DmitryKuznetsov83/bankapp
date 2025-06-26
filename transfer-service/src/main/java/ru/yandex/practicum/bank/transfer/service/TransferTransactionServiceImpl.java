package ru.yandex.practicum.bank.transfer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.yandex.practicum.bank.common.dto.ApiErrorDto;
import ru.yandex.practicum.bank.notification.service.NotificationSender;
import ru.yandex.practicum.bank.transfer.dto.CreateTransferTransactionDto;
import ru.yandex.practicum.bank.transfer.dto.RelativeExchangeRateDto;
import ru.yandex.practicum.bank.transfer.dto.TransferTransactionDto;
import ru.yandex.practicum.bank.transfer.enums.TransactionStatus;
import ru.yandex.practicum.bank.transfer.mapper.TransferTransactionMapper;
import ru.yandex.practicum.bank.transfer.model.TransferTransaction;
import ru.yandex.practicum.bank.transfer.repository.TransferTransactionJpaRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static ru.yandex.practicum.bank.notification.enums.NotificationLevel.INFO;
import static ru.yandex.practicum.bank.notification.enums.NotificationLevel.WARNING;
import static ru.yandex.practicum.bank.transfer.enums.TransactionStatus.*;

@Service
public class TransferTransactionServiceImpl implements TransferTransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransferTransactionServiceImpl.class);

    private final RestTemplate internalRestTemplate;
    private final TransferTransactionJpaRepository transferTransactionJpaRepository;
    private final NotificationSender notificationSender;

    public TransferTransactionServiceImpl(RestTemplate internalRestTemplate, TransferTransactionJpaRepository transferTransactionJpaRepository, NotificationSender notificationSender) {
        this.internalRestTemplate = internalRestTemplate;
        this.transferTransactionJpaRepository = transferTransactionJpaRepository;
        this.notificationSender = notificationSender;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransferTransactionDto> getTransferTransactions(String login) {
        return transferTransactionJpaRepository
                .findByFromLoginOrToLogin(login, login)
                .stream()
                .map(TransferTransactionMapper.INSTANCE::toTransferTransactionDto)
                .toList();
    }

    @Override
    @Transactional
    public TransferTransactionDto createTransferTransaction(CreateTransferTransactionDto createTransferTransactionDto) {
        TransferTransaction transaction = TransferTransactionMapper.INSTANCE.toTransferTransaction(createTransferTransactionDto);
        transaction = updateTransactionStatus(transaction, PENDING, null);
        transaction = calculateToSum(transaction);
        if (PENDING.equals(transaction.getStatus())) {
            transaction = checkBlocking(transaction);
        }
        if (PENDING.equals(transaction.getStatus())) {
            transaction = checkBalance(transaction);
        }
        return TransferTransactionMapper.INSTANCE.toTransferTransactionDto(transaction);
    }

    private TransferTransaction calculateToSum(TransferTransaction transaction) {
        if (transaction.getFromCurrency().equals(transaction.getToCurrency())) {
            transaction.setToSum(transaction.getFromSum());
            transaction = transferTransactionJpaRepository.save(transaction);
            return transaction;
        }

        try {
            String url = UriComponentsBuilder.fromUriString("lb://api-gateway/api/exchange-service/rates/relative")
                    .queryParam("fromCurrency", transaction.getFromCurrency())
                    .queryParam("toCurrency", transaction.getToCurrency())
                    .toUriString();

            BigDecimal toSum = internalRestTemplate
                    .getForObject(url, RelativeExchangeRateDto.class)
                    .getRate()
                    .multiply(transaction.getFromSum()).setScale(0, RoundingMode.UP);

            transaction.setToSum(toSum);
            transaction = transferTransactionJpaRepository.save(transaction);
        } catch (HttpClientErrorException e) {
            String reason = e.getResponseBodyAs(ApiErrorDto.class).getMessage();
            updateTransactionStatus(transaction, FAILED, reason);
            notificationSender.send(transaction.getFromLogin(), WARNING, reason);
        } catch (Throwable e) {
            String reason = "Сервис курсов недоступен";
            log.warn(reason, e);
            updateTransactionStatus(transaction, FAILED, reason);
            notificationSender.send(transaction.getFromLogin(), WARNING, reason);
        }
        return transaction;
    }

    private TransferTransaction checkBlocking(TransferTransaction transaction) {
        try {
            TransferTransactionDto transferTransactionDto = TransferTransactionMapper.INSTANCE.toTransferTransactionDto(transaction);
            Boolean blocked = internalRestTemplate
                    .postForObject("lb://api-gateway/api/blocker-service/blockers/transfer-transactions/validate", transferTransactionDto, Boolean.class);
            if (Boolean.TRUE.equals(blocked)) {
                String reason = "Транзакция заблокирована как подозрительная";
                transaction = updateTransactionStatus(transaction, FAILED, reason);
                notificationSender.send(transaction.getFromLogin(), WARNING, reason);
            }
        } catch (Throwable e) {
            log.warn("Сервис блокировок недоступен", e);
        }
        return transaction;
    }

    private TransferTransaction checkBalance(TransferTransaction transaction) {
        try {
            TransferTransactionDto transferTransactionDto = TransferTransactionMapper.INSTANCE.toTransferTransactionDto(transaction);
            internalRestTemplate
                    .postForEntity("lb://api-gateway/api/account-service/transactions/transfer-transactions/validate", transferTransactionDto, Void.class);
            transaction = updateTransactionStatus(transaction, SUCCESS, null);
            notificationSender.send(transaction.getFromLogin(), INFO, "Успешный перевод средств");
        } catch (HttpClientErrorException e) {
            String reason = e.getResponseBodyAs(ApiErrorDto.class).getMessage();
            transaction = updateTransactionStatus(transaction, FAILED, reason);
            notificationSender.send(transaction.getFromLogin(), WARNING, reason);
        } catch (Throwable e) {
            String reason = "Сервис валидации транзакций недоступен";
            log.warn(reason, e);
            transaction = updateTransactionStatus(transaction, FAILED, reason);
            notificationSender.send(transaction.getFromLogin(), WARNING, reason);
        }
        return transaction;
    }

    private TransferTransaction updateTransactionStatus(TransferTransaction transaction, TransactionStatus status, String comment) {
        transaction.setStatus(status);
        if (comment != null) {
            transaction.setComment(comment);
        }
        return transferTransactionJpaRepository.save(transaction);
    }

}

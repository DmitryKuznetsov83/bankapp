package ru.yandex.practicum.bank.transfer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.yandex.practicum.bank.notification.service.NotificationSender;
import ru.yandex.practicum.bank.transfer.dto.ApiErrorDto;
import ru.yandex.practicum.bank.transfer.dto.CreateTransferTransactionDto;
import ru.yandex.practicum.bank.transfer.dto.RelativeExchangeRateDto;
import ru.yandex.practicum.bank.transfer.dto.TransferTransactionDto;
import ru.yandex.practicum.bank.transfer.enums.Currency;
import ru.yandex.practicum.bank.transfer.enums.TransactionStatus;
import ru.yandex.practicum.bank.transfer.mapper.TransferTransactionMapper;
import ru.yandex.practicum.bank.transfer.model.TransferTransaction;
import ru.yandex.practicum.bank.transfer.repository.TransferTransactionJpaRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static ru.yandex.practicum.bank.notification.enums.NotificationLevel.INFO;
import static ru.yandex.practicum.bank.notification.enums.NotificationLevel.WARNING;

@Service
public class TransferTransactionServiceImpl implements TransferTransactionService {

    private final RestClient restClient = RestClient.create();

    private final TransferTransactionJpaRepository transferTransactionJpaRepository;
    private final NotificationSender notificationSender;

    public TransferTransactionServiceImpl(TransferTransactionJpaRepository transferTransactionJpaRepository, NotificationSender notificationSender) {
        this.transferTransactionJpaRepository = transferTransactionJpaRepository;
        this.notificationSender = notificationSender;
    }

    @Override
    public List<TransferTransactionDto> getTransferTransactions(String login) {
        return transferTransactionJpaRepository
                .findByFromLoginOrToLogin(login, login)
                .stream()
                .map(TransferTransactionMapper.INSTANCE::toTransferTransactionDto)
                .toList();
    }

    @Override
    public TransferTransactionDto createTransferTransaction(CreateTransferTransactionDto createTransferTransactionDto) {

        TransferTransaction transaction = TransferTransactionMapper.INSTANCE.toTransferTransaction(createTransferTransactionDto);
        transaction.setStatus(TransactionStatus.PENDING);

        Currency fromCurrency = createTransferTransactionDto.getFromCurrency();
        Currency toCurrency = createTransferTransactionDto.getToCurrency();

        try {
            String url = UriComponentsBuilder.fromUriString("http://localhost:8084/rates/relative")
                    .queryParam("fromCurrency", fromCurrency)
                    .queryParam("toCurrency", toCurrency)
                    .toUriString();

            BigDecimal toSum = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(RelativeExchangeRateDto.class)
                    .getRate()
                    .multiply(transaction.getFromSum()).setScale(0, RoundingMode.UP);

            transaction.setToSum(toSum);
            transaction = transferTransactionJpaRepository.save(transaction);
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            String reason = apiErrorDto.getMessage();
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setComment(reason);
            transaction = transferTransactionJpaRepository.save(transaction);
            notificationSender.send(createTransferTransactionDto.getFromLogin(), WARNING, reason);
            return TransferTransactionMapper.INSTANCE.toTransferTransactionDto(transaction);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }

        transaction = transferTransactionJpaRepository.save(transaction);
        TransferTransactionDto transactionDto = TransferTransactionMapper.INSTANCE.toTransferTransactionDto(transaction);



        try {
            Boolean blocked = restClient
                    .post()
                    .uri("http://localhost:8087/blocker/transfer-transactions")
                    .body(transactionDto)
                    .retrieve()
                    .body(Boolean.class);
            if (Boolean.TRUE.equals(blocked)) {
                transaction.setStatus(TransactionStatus.FAILED);
                transaction.setComment("Транзакция заблокирована как подозрительная");
                notificationSender.send(createTransferTransactionDto.getFromLogin(), WARNING, "Транзакция заблокирована как подозрительная");
                return TransferTransactionMapper.INSTANCE.toTransferTransactionDto(transaction);
            } else {
                transaction.setStatus(TransactionStatus.SUCCESS);
            }
            transaction = transferTransactionJpaRepository.save(transaction);
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            String reason = apiErrorDto.getMessage();
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setComment(reason);
            transaction = transferTransactionJpaRepository.save(transaction);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }




        try {
            restClient
                    .post()
                    .uri("http://localhost:8082/transactions/transfer")
                    .body(transactionDto)
                    .retrieve()
                    .toBodilessEntity();
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction = transferTransactionJpaRepository.save(transaction);
            notificationSender.send(createTransferTransactionDto.getFromLogin(), INFO, "Успешный перевод средств");
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            String reason = apiErrorDto.getMessage();
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setComment(reason);
            transaction = transferTransactionJpaRepository.save(transaction);
            notificationSender.send(createTransferTransactionDto.getFromLogin(), WARNING, reason);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
        return TransferTransactionMapper.INSTANCE.toTransferTransactionDto(transaction);
    }

}

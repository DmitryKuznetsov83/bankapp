package ru.yandex.practicum.bank.transfer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
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

@Service
public class TransferTransactionServiceImpl implements TransferTransactionService {

    private final RestClient restClient = RestClient.create();

    private final TransferTransactionJpaRepository transferTransactionJpaRepository;

    public TransferTransactionServiceImpl(TransferTransactionJpaRepository transferTransactionJpaRepository) {
        this.transferTransactionJpaRepository = transferTransactionJpaRepository;
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
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }

        transaction = transferTransactionJpaRepository.save(transaction);
        TransferTransactionDto transactionDto = TransferTransactionMapper.INSTANCE.toTransferTransactionDto(transaction);
        try {
            restClient
                    .post()
                    .uri("http://localhost:8082/transactions/transfer")
                    .body(transactionDto)
                    .retrieve()
                    .toBodilessEntity();
            transaction.setStatus(TransactionStatus.SUCCESS);
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
        return TransferTransactionMapper.INSTANCE.toTransferTransactionDto(transaction);
    }

}

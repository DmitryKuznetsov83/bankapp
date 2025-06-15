package ru.yandex.practicum.bank.cash.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.bank.cash.dto.ApiErrorDto;
import ru.yandex.practicum.bank.cash.dto.CashTransactionDto;
import ru.yandex.practicum.bank.cash.dto.CreateCashTransactionDto;
import ru.yandex.practicum.bank.cash.enums.TransactionStatus;
import ru.yandex.practicum.bank.cash.mapper.CashTransactionMapper;
import ru.yandex.practicum.bank.cash.model.CashTransaction;
import ru.yandex.practicum.bank.cash.repository.CashTransactionJpaRepository;

import java.util.List;

@Service
public class CashTransactionServiceImpl implements CashTransactionService {

    private final RestClient restClient = RestClient.create();

    private final CashTransactionJpaRepository cashTransactionJpaRepository;

    public CashTransactionServiceImpl(CashTransactionJpaRepository cashTransactionJpaRepository) {
        this.cashTransactionJpaRepository = cashTransactionJpaRepository;
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
        transaction.setStatus(TransactionStatus.PENDING);
        transaction = cashTransactionJpaRepository.save(transaction);
        CashTransactionDto transactionDto = CashTransactionMapper.INSTANCE.toCashTransactionDto(transaction);
        try {
            restClient
                    .post()
                    .uri("http://localhost:8082/transactions/cash")
                    .body(transactionDto)
                    .retrieve()
                    .toBodilessEntity();
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction = cashTransactionJpaRepository.save(transaction);
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            String reason = apiErrorDto.getMessage();
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setComment(reason);
            transaction = cashTransactionJpaRepository.save(transaction);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
        return CashTransactionMapper.INSTANCE.toCashTransactionDto(transaction);
    }
}

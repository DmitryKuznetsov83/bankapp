package ru.yandex.practicum.bank.cash.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.bank.cash.dto.CashTransactionDto;
import ru.yandex.practicum.bank.cash.dto.CreateCashTransactionDto;
import ru.yandex.practicum.bank.cash.exception.UnsuccessfulTransactionException;
import ru.yandex.practicum.bank.cash.service.CashTransactionService;

import java.util.List;

import static ru.yandex.practicum.bank.cash.enums.TransactionStatus.*;

@RestController
@RequestMapping("/cash-transactions")
@Validated
public class CashTransactionController {

    private final CashTransactionService cashTransactionService;

    @Autowired
    public CashTransactionController(CashTransactionService cashTransactionService) {
        this.cashTransactionService = cashTransactionService;
    }

    @GetMapping("/{login}")
    public List<CashTransactionDto> getCashTransactions(@PathVariable String login) {
        return cashTransactionService.getCashTransactions(login);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CashTransactionDto createCashTransaction(@RequestBody @Valid CreateCashTransactionDto createCashTransactionDto) {
        CashTransactionDto cashTransactionDto = cashTransactionService.createCashTransaction(createCashTransactionDto);
        if (!SUCCESS.equals(cashTransactionDto.getStatus())) {
            throw new UnsuccessfulTransactionException(cashTransactionDto.getComment());
        } else {
            return cashTransactionDto;
        }
    }

}

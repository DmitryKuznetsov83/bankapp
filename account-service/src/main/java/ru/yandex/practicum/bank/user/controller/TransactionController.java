package ru.yandex.practicum.bank.user.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.bank.user.dto.ApiErrorDto;
import ru.yandex.practicum.bank.user.dto.transaction.CashTransactionDto;
import ru.yandex.practicum.bank.user.dto.transaction.TransferTransactionDto;
import ru.yandex.practicum.bank.user.exception.account.InsufficientFundsException;
import ru.yandex.practicum.bank.user.service.TransactionService;


@RestController
@RequestMapping("/transactions")
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/cash")
    public void processCashTransaction(@RequestBody @Valid CashTransactionDto cashTransactionDto) {
        transactionService.processTransaction(cashTransactionDto);
    }

    @PostMapping("/transfer")
    public void processTransferTransaction(@RequestBody @Valid TransferTransactionDto transferTransactionDto) {
        transactionService.processTransaction(transferTransactionDto);
    }

    @ExceptionHandler({InsufficientFundsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorDto handleInsufficientFundsException(final InsufficientFundsException exception) {
        return GlobalExceptionHandler.getApiError(exception, HttpStatus.CONFLICT);
    }

}

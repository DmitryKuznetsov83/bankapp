package ru.yandex.practicum.bank.transfer.controller;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.bank.transfer.dto.CreateTransferTransactionDto;
import ru.yandex.practicum.bank.transfer.dto.TransferTransactionDto;
import ru.yandex.practicum.bank.transfer.enums.TransactionStatus;
import ru.yandex.practicum.bank.transfer.exception.UnsuccessfulTransactionException;
import ru.yandex.practicum.bank.transfer.service.TransferTransactionService;

import java.util.List;

@RestController
@RequestMapping("/transfer-transactions")
@Validated
public class TransferTransactionController {

    private final TransferTransactionService transferTransactionService;

    public TransferTransactionController(TransferTransactionService transferTransactionService) {
        this.transferTransactionService = transferTransactionService;
    }

    @GetMapping("/{login}")
    public List<TransferTransactionDto> getTransferTransactions(@PathVariable String login) {
        return transferTransactionService.getTransferTransactions(login);
    }

    @PostMapping("/self-transactions")
    public TransferTransactionDto createSelfTransferTransaction(@RequestBody @Valid CreateTransferTransactionDto createTransferTransactionDto) {
        TransferTransactionDto transferTransactionDto = transferTransactionService.createTransferTransaction(createTransferTransactionDto);
        if (!TransactionStatus.SUCCESS.equals(transferTransactionDto.getStatus())) {
            throw new UnsuccessfulTransactionException(transferTransactionDto.getComment());
        } else {
            return transferTransactionDto;
        }
    }

    @PostMapping("/external-transactions")
    public TransferTransactionDto createExternalTransferTransaction(@RequestBody @Valid CreateTransferTransactionDto createTransferTransactionDto) {
        TransferTransactionDto transferTransactionDto = transferTransactionService.createTransferTransaction(createTransferTransactionDto);
        if (!TransactionStatus.SUCCESS.equals(transferTransactionDto.getStatus())) {
            throw new UnsuccessfulTransactionException(transferTransactionDto.getComment());
        } else {
            return transferTransactionDto;
        }
    }

}

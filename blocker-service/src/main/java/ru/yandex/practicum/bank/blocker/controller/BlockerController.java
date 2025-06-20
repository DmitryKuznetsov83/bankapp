package ru.yandex.practicum.bank.blocker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.bank.blocker.dto.CashTransactionDto;
import ru.yandex.practicum.bank.blocker.dto.TransferTransactionDto;
import ru.yandex.practicum.bank.blocker.service.BlockerService;

@RestController
@RequestMapping("/blockers")
public class BlockerController {

    private final BlockerService blockerService;

    @Autowired
    public BlockerController(BlockerService blockerService) {
        this.blockerService = blockerService;
    }

    @PostMapping("/cash-transactions/validate")
    Boolean validateCashTransaction(@RequestBody CashTransactionDto cashTransactionDto) {
        return blockerService.validateCashTransaction(cashTransactionDto);
    }

    @PostMapping("/transfer-transactions/validate")
    Boolean validateTransferTransaction(@RequestBody TransferTransactionDto transferTransactionDto) {
        return blockerService.validateTransferTransaction(transferTransactionDto);
    }

}

package ru.yandex.practicum.bank.blocker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.bank.blocker.dto.CashTransactionDto;
import ru.yandex.practicum.bank.blocker.dto.TransferTransactionDto;
import ru.yandex.practicum.bank.blocker.service.BlockerService;

@RestController
@RequestMapping("/blocker")
public class BlockerController {

    private final BlockerService blockerService;

    @Autowired
    public BlockerController(BlockerService blockerService) {
        this.blockerService = blockerService;
    }

    @PostMapping("/cash-transactions")
    Boolean blockCashTransaction(@RequestBody CashTransactionDto cashTransactionDto) {
        return blockerService.blockCashTransaction(cashTransactionDto);
    }

    @PostMapping("/transfer-transactions")
    Boolean blockTransferTransaction(@RequestBody TransferTransactionDto transferTransactionDto) {
        return blockerService.blockTransferTransaction(transferTransactionDto);
    }

}

package ru.yandex.practicum.bank.blocker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.bank.blocker.dto.CashTransactionDto;
import ru.yandex.practicum.bank.blocker.dto.TransferTransactionDto;
import ru.yandex.practicum.bank.blocker.service.BlockerService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/blockers")
public class BlockerController {

    private final BlockerService blockerService;

    @Autowired
    public BlockerController(BlockerService blockerService) {
        this.blockerService = blockerService;
    }

    @PostMapping(value = "/cash-transactions/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Boolean> validateCashTransaction(@RequestBody CashTransactionDto cashTransactionDto) {
        boolean isValid = blockerService.isValidTransaction(cashTransactionDto);
        return Collections.singletonMap("valid", isValid);
    }

    @PostMapping(value = "/transfer-transactions/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Boolean> validateTransferTransaction(@RequestBody TransferTransactionDto transferTransactionDto) {
        boolean isValid = blockerService.isValidTransaction(transferTransactionDto);
        return Collections.singletonMap("valid", isValid);
    }

}

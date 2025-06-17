package ru.yandex.practicum.bank.blocker.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.bank.blocker.config.BlockerConfig;
import ru.yandex.practicum.bank.blocker.dto.CashTransactionDto;
import ru.yandex.practicum.bank.blocker.dto.TransferTransactionDto;

import java.math.BigDecimal;

@Service
public class BlockerServiceImpl implements BlockerService {
    
    private final BlockerConfig blockerConfig;

    public BlockerServiceImpl(BlockerConfig blockerConfig) {
        this.blockerConfig = blockerConfig;
    }

    @Override
    public Boolean blockCashTransaction(CashTransactionDto cashTransactionDto) {
        BigDecimal transactionSum = cashTransactionDto.getSum();
        BigDecimal min = blockerConfig.getMinThreshold();
        BigDecimal max = blockerConfig.getMaxThreshold();
        return transactionSum.compareTo(min) < 0 || transactionSum.compareTo(max) > 0;
    }

    @Override
    public Boolean blockTransferTransaction(TransferTransactionDto transferTransactionDto) {
        BigDecimal transactionSum = transferTransactionDto.getFromSum();
        BigDecimal min = blockerConfig.getMinThreshold();
        BigDecimal max = blockerConfig.getMaxThreshold();
        return transactionSum.compareTo(min) < 0 || transactionSum.compareTo(max) > 0;
    }
}

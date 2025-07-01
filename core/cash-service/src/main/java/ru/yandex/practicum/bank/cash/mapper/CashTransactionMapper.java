package ru.yandex.practicum.bank.cash.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.bank.cash.dto.CashTransactionDto;
import ru.yandex.practicum.bank.cash.dto.CreateCashTransactionDto;
import ru.yandex.practicum.bank.cash.model.CashTransaction;

@Mapper
public interface CashTransactionMapper {

    CashTransactionMapper INSTANCE = Mappers.getMapper(CashTransactionMapper.class);

    CashTransaction toCashTransaction(CreateCashTransactionDto createCashTransactionDto);

    CashTransactionDto toCashTransactionDto(CashTransaction cashTransaction);

}

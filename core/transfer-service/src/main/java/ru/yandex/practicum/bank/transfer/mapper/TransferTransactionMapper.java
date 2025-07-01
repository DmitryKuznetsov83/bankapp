package ru.yandex.practicum.bank.transfer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.bank.transfer.dto.CreateTransferTransactionDto;
import ru.yandex.practicum.bank.transfer.dto.TransferTransactionDto;
import ru.yandex.practicum.bank.transfer.model.TransferTransaction;

@Mapper
public interface TransferTransactionMapper {

    TransferTransactionMapper INSTANCE = Mappers.getMapper(TransferTransactionMapper.class);

    TransferTransactionDto toTransferTransactionDto(TransferTransaction cashTransaction);

    TransferTransaction toTransferTransaction(CreateTransferTransactionDto createTransferTransactionDto);

}

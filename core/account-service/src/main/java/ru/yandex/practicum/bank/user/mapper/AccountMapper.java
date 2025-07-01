package ru.yandex.practicum.bank.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.bank.user.dto.account.AccountDto;
import ru.yandex.practicum.bank.user.model.Account;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "owner", source = "owner.login")
    AccountDto toAccountDto(Account account);

}

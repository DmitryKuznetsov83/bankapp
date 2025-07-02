package ru.yandex.practicum.bank.user.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.bank.user.enums.AccountStateChangeRequest;
import ru.yandex.practicum.bank.user.enums.Currency;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountsChangeRequestDto {

    private Currency currency;
    private AccountStateChangeRequest accountChangeRequest;

}

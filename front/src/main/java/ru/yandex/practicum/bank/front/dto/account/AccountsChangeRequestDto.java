package ru.yandex.practicum.bank.front.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.bank.front.enums.Currency;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountsChangeRequestDto {

    private Currency currency;
    private AccountStateChangeRequest accountChangeRequest;

    public AccountsChangeRequestDto(boolean active, String currency) {
        this.accountChangeRequest = (active ? AccountStateChangeRequest.OPEN : AccountStateChangeRequest.CLOSE);
        this.currency = Currency.valueOf(currency);
    }
}

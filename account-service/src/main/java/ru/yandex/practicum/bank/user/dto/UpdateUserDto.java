package ru.yandex.practicum.bank.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.bank.user.annotation.IsBirthdayOfAdult;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    @NotNull
    @Length(min = 5, max = 32)
    private String fullName;

    @NotNull
    @IsBirthdayOfAdult
    private LocalDate birthdate;

}

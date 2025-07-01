package ru.yandex.practicum.bank.user.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.bank.user.annotation.IsBirthdayOfAdult;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    @NotNull
    @Length(min = 5, max = 32)
    private String name;

    @NotNull
    @IsBirthdayOfAdult
    private LocalDate birthdate;

}

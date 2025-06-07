package ru.yandex.practicum.bank.user.dto;

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
public class CreateUserDto {

    @NotNull
    @Length(min = 5, max = 16, message = "login должен быть от 5 до 16 символов")
    private String login;

    @NotNull
    @Length(min = 5, max = 16)
    private String password;

    @NotNull
    @Length(min = 5, max = 16)
    private String confirmPassword;

    @NotNull
    @Length(min = 5, max = 32)
    private String fullName;

    @NotNull
    @IsBirthdayOfAdult
    private LocalDate birthdate;

}
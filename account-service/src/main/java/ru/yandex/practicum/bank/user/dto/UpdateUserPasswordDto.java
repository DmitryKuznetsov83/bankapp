package ru.yandex.practicum.bank.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserPasswordDto {

    @NotNull
    @Length(min = 5, max = 16)
    private String password;

    @NotNull
    @Length(min = 5, max = 16)
    private String confirmPassword;

}

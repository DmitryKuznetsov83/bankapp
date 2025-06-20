package ru.yandex.practicum.bank.front.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserForm {

    private String login;
    private String password;
    private String confirmPassword;
    private String name;
    private LocalDate birthdate;

}
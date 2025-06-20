package ru.yandex.practicum.bank.front.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.bank.common.dto.ApiErrorDto;
import ru.yandex.practicum.bank.front.dto.user.CreateUserForm;
import ru.yandex.practicum.bank.front.dto.user.UserDto;
import ru.yandex.practicum.bank.front.mapper.UserMapper;

import java.util.List;

@Controller
public class SignupController {

    private final RestClient restClient = RestClient.create();
    private final PasswordEncoder passwordEncoder;

    public SignupController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/signup")
    public String getSignupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String createUser(Model model, @ModelAttribute CreateUserForm createUserForm) {

        String password = createUserForm.getPassword();
        String confirmPassword = createUserForm.getConfirmPassword();

        if (!password.equals(confirmPassword)) {
            model.addAttribute("errors", "Пароль и подтверждение на совпадают");
            return "signup";
        }

        String passwordHash = passwordEncoder.encode(password);
        UserDto userDto = UserMapper.INSTANCE.toUserDto(createUserForm);
        userDto.setPasswordHash(passwordHash);

        try {
            restClient
                    .post()
                    .uri("http://localhost:8082/users")
                    .body(userDto)
                    .retrieve()
                    .toBodilessEntity();
            return "redirect:/login";
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            model.addAttribute("errors", List.of(apiErrorDto.getMessage()));
            return "signup";
        }
    }

}

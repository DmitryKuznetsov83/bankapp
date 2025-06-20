package ru.yandex.practicum.bank.front.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bank.common.dto.ApiErrorDto;
import ru.yandex.practicum.bank.front.dto.user.CreateUserForm;
import ru.yandex.practicum.bank.front.dto.user.UserDto;
import ru.yandex.practicum.bank.front.mapper.UserMapper;

import java.util.List;

@Controller
public class SignupController {

    private final RestTemplate internalRestTemplate;
    private final PasswordEncoder passwordEncoder;

    public SignupController(RestTemplate internalRestTemplate, PasswordEncoder passwordEncoder) {
        this.internalRestTemplate = internalRestTemplate;
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
            internalRestTemplate
                    .postForObject("http://localhost:8080/users", userDto, String.class);
            return "redirect:/login";
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            model.addAttribute("errors", List.of(apiErrorDto.getMessage()));
            return "signup";
        }
    }

}

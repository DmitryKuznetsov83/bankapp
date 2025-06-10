package ru.yandex.practicum.bank.front.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.yandex.practicum.bank.front.dto.*;
import ru.yandex.practicum.bank.front.mapper.UserMapper;
import ru.yandex.practicum.bank.front.service.user.AppUserDetails;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
public class MainController {

    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserDetailsService userDetailsService;

    @Autowired
    public MainController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @GetMapping("/main")
    public String getMainPage(Model model, @AuthenticationPrincipal AppUserDetails appUserDetails) {
        model.addAttribute("login", appUserDetails.getUsername());
        model.addAttribute("name", appUserDetails.getName());
        model.addAttribute("birthdate", appUserDetails.getBirthdate());
        return "main";
    }

    @GetMapping("/")
    public String redirectToMainPage() {
        return "redirect:/main";
    }

    @GetMapping("/signup")
    public String getSignupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String createNewUser(Model model, @ModelAttribute CreateUserDto createUserDto) {
        try {
            restClient
                    .post()
                    .uri("http://localhost:8082/users")
                    .body(createUserDto)
                    .retrieve()
                    .body(UserDto.class);
            return "redirect:/login";
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            model.addAttribute("errors", List.of(apiErrorDto.getMessage()));
            return "signup";
        }
    }

    @PostMapping("/user/editPassword")
    public String changeUserPassword(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                     Model model,
                                     @ModelAttribute UpdateUserPasswordDto updateUserPasswordDto,
                                     RedirectAttributes redirectAttributes) {
        try {
            restClient
                    .put()
                    .uri("http://localhost:8082/users/" + appUserDetails.getLogin() + "/password")
                    .body(updateUserPasswordDto)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            redirectAttributes.addFlashAttribute("passwordErrors", List.of(apiErrorDto.getMessage()));
        }
        return "redirect:/main";
    }

    @PostMapping("/user/editUser")
    public String changeUser(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                     Model model,
                                     @ModelAttribute UpdateUserDto updateUserDto,
                                     RedirectAttributes redirectAttributes) {
        try {
            restClient
                    .put()
                    .uri("http://localhost:8082/users/" + appUserDetails.getLogin())
                    .body(updateUserDto)
                    .retrieve()
                    .toBodilessEntity();

            AppUserDetails newDetails = (AppUserDetails) userDetailsService.loadUserByUsername(appUserDetails.getUsername());

            var newAuth = new UsernamePasswordAuthenticationToken(
                    newDetails,
                    null,
                    newDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            redirectAttributes.addFlashAttribute("userErrors", List.of(apiErrorDto.getMessage()));
        }

        return "redirect:/main";
    }

}

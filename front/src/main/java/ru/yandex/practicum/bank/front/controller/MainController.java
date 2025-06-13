package ru.yandex.practicum.bank.front.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.yandex.practicum.bank.front.dto.*;
import ru.yandex.practicum.bank.front.dto.account.AccountDto;
import ru.yandex.practicum.bank.front.dto.account.AccountsChangeRequestDto;
import ru.yandex.practicum.bank.front.dto.user.CreateUserDto;
import ru.yandex.practicum.bank.front.dto.user.UpdateUserDto;
import ru.yandex.practicum.bank.front.dto.user.UpdateUserPasswordDto;
import ru.yandex.practicum.bank.front.dto.user.UserDto;
import ru.yandex.practicum.bank.front.enums.AccountState;
import ru.yandex.practicum.bank.front.enums.Currency;
import ru.yandex.practicum.bank.front.service.user.AppUserDetails;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        // user
        model.addAttribute("login", appUserDetails.getUsername());
        model.addAttribute("name", appUserDetails.getName());
        model.addAttribute("birthdate", appUserDetails.getBirthdate());

        // accounts
        try {
            List<AccountDto> accountDtos = restClient
                    .get()
                    .uri("http://localhost:8082/accounts/"+appUserDetails.getUsername())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<AccountDto>>() {});
            List<AccountRepresentation> list = accountDtos.stream()
                    .map(a -> new AccountRepresentation(a.getCurrency(), a.getState(), a.getBalance()))
                    .toList();
            Map<Currency, AccountRepresentation> collect = list.stream().collect(Collectors.toMap(l -> l.getCurrency(), l -> l));

            Arrays.stream(Currency.values()).forEach(c -> {
                if (!collect.containsKey(c)) {
                    collect.put(c, new AccountRepresentation(c, AccountState.NOT_EXISTS, BigDecimal.ZERO));
                }
            });

            List<AccountRepresentation> accountRepresentations = new ArrayList<>(collect.values());
            accountRepresentations.sort(Comparator.comparing(
                    AccountRepresentation::getCurrency,
                    Comparator.comparingInt(Enum::ordinal)));
            model.addAttribute("accountRepresentations", accountRepresentations);

        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            model.addAttribute("userAccountsErrors", List.of(apiErrorDto.getMessage()));
        }

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

    @PostMapping("/accounts/changeState")
    public String changeAccountsState(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                      Model model,
                                      @ModelAttribute AccountStateChangeForm form,
                                      RedirectAttributes redirectAttributes) {

        List<AccountStateChangeInterfaceDto> accountsIner = form.getAccounts();
        Stream<AccountsChangeRequestDto> accounts = accountsIner.stream().map(q -> new AccountsChangeRequestDto(q.isActive(), q.getCurrency()));

        try {
            restClient
                    .post()
                    .uri("http://localhost:8082/accounts/" + appUserDetails.getLogin())
                    .body(accounts)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<AccountDto>>() {});
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            redirectAttributes.addFlashAttribute("userAccountsErrors", List.of(apiErrorDto.getMessage()));
        }

        return "redirect:/main";
    }


}

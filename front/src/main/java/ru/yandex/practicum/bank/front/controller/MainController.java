package ru.yandex.practicum.bank.front.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.yandex.practicum.bank.front.dto.*;
import ru.yandex.practicum.bank.front.dto.account.AccountDto;
import ru.yandex.practicum.bank.front.dto.account.AccountsChangeRequestDto;
import ru.yandex.practicum.bank.front.dto.transaction.CreateCashTransactionDto;
import ru.yandex.practicum.bank.front.dto.transaction.CreateTransferTransactionDto;
import ru.yandex.practicum.bank.front.dto.user.*;
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

        model.addAttribute("currency", Currency.values());


        List<ShortUserDto> shortUserDtos = new ArrayList<>();
        try {
            shortUserDtos = restClient
                    .get()
                    .uri("http://localhost:8082/users")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ShortUserDto>>() {});

        } catch (Throwable e) {
            model.addAttribute("externalTransferErrors", List.of("Список пользователей недоступен"));
        }

        model.addAttribute("users", shortUserDtos);

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

    @PostMapping("/user/cash")
    public String processCashTransaction(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                      Model model,
                                      @ModelAttribute CreateCashTransactionDto createCashTransactionDto,
                                      RedirectAttributes redirectAttributes) {

        createCashTransactionDto.setUserLogin(appUserDetails.getLogin());

        try {
            restClient
                    .post()
                    .uri("http://localhost:8085/cash-transactions")
                    .body(createCashTransactionDto)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            redirectAttributes.addFlashAttribute("cashErrors", List.of(apiErrorDto.getMessage()));
        } catch (Throwable e) {
            redirectAttributes.addFlashAttribute("cashErrors", List.of("Сервис недоступен"));
        }

        return "redirect:/main";
    }

    @PostMapping("/user/self-transfer")
    public String processSelfTransferTransaction(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                         Model model,
                                         @ModelAttribute CreateTransferTransactionDto createTransferTransactionDto,
                                         RedirectAttributes redirectAttributes) {

        createTransferTransactionDto.setFromLogin(appUserDetails.getLogin());
        createTransferTransactionDto.setToLogin(appUserDetails.getLogin());

        try {
            restClient
                    .post()
                    .uri("http://localhost:8086/transfer-transactions/self-transactions")
                    .body(createTransferTransactionDto)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            redirectAttributes.addFlashAttribute("selfTransferErrors", List.of(apiErrorDto.getMessage()));
        } catch (Throwable e) {
            redirectAttributes.addFlashAttribute("selfTransferErrors", List.of("Сервис недоступен"));
        }

        return "redirect:/main";
    }

    @PostMapping("/user/external-transfer")
    public String processExternalTransferTransaction(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                                 Model model,
                                                 @ModelAttribute CreateTransferTransactionDto creatTransferTransactionDto,
                                                 RedirectAttributes redirectAttributes) {

        creatTransferTransactionDto.setFromLogin(appUserDetails.getLogin());

        try {
            restClient
                    .post()
                    .uri("http://localhost:8086/transfer-transactions/external-transactions")
                    .body(creatTransferTransactionDto)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            redirectAttributes.addFlashAttribute("externalTransferErrors", List.of(apiErrorDto.getMessage()));
        } catch (Throwable e) {
            redirectAttributes.addFlashAttribute("externalTransferErrors", List.of("Сервис недоступен"));
        }

        return "redirect:/main";
    }

}

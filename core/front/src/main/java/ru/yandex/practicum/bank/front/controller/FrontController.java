package ru.yandex.practicum.bank.front.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.yandex.practicum.bank.common.dto.ApiErrorDto;
import ru.yandex.practicum.bank.front.config.BackendProperties;
import ru.yandex.practicum.bank.front.dto.account.*;
import ru.yandex.practicum.bank.front.dto.transaction.CreateCashTransactionDto;
import ru.yandex.practicum.bank.front.dto.transaction.CreateTransferTransactionDto;
import ru.yandex.practicum.bank.front.dto.user.*;
import ru.yandex.practicum.bank.front.enums.AccountState;
import ru.yandex.practicum.bank.front.enums.Currency;
import ru.yandex.practicum.bank.front.security.AppUserDetails;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class FrontController {

    private final RestTemplate externalRestTemplate;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final BackendProperties backendProperties;

    @Autowired
    public FrontController(RestTemplate externalRestTemplate,
                           UserDetailsService userDetailsService,
                           PasswordEncoder passwordEncoder, BackendProperties backendProperties) {
        this.externalRestTemplate = externalRestTemplate;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.backendProperties = backendProperties;
    }


    @GetMapping("/main")
    public String getMainPage(Model model, @AuthenticationPrincipal AppUserDetails appUserDetails) {
        // user
        model.addAttribute("login", appUserDetails.getUsername());
        model.addAttribute("name", appUserDetails.getName());
        model.addAttribute("birthdate", appUserDetails.getBirthdate());

        // currencies
        model.addAttribute("currency", Currency.values());

        // accounts
        try {
            List<AccountDto> accountDtos = externalRestTemplate
                    .exchange(
                            backendProperties.getUrl() + "/api/account-service/accounts/" + appUserDetails.getUsername(),
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<AccountDto>>() {
                            })
                    .getBody();
            List<AccountView> list = accountDtos.stream()
                    .map(a -> new AccountView(a.getCurrency(), a.getState(), a.getBalance()))
                    .toList();
            Map<Currency, AccountView> collect = list.stream().collect(Collectors.toMap(l -> l.getCurrency(), l -> l));

            Arrays.stream(Currency.values()).forEach(c -> {
                if (!collect.containsKey(c)) {
                    collect.put(c, new AccountView(c, AccountState.NOT_EXISTS, BigDecimal.ZERO));
                }
            });

            List<AccountView> accountRepresentations = new ArrayList<>(collect.values());
            accountRepresentations.sort(Comparator.comparing(
                    AccountView::getCurrency,
                    Comparator.comparingInt(Enum::ordinal)));
            model.addAttribute("accountRepresentations", accountRepresentations);

        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            model.addAttribute("userAccountsErrors", List.of(apiErrorDto.getMessage()));
        } catch (Throwable t) {
            System.out.println(t);
        }

        // users (correspondents)
        List<ShortUserDto> shortUserDtos = new ArrayList<>();
        try {
            shortUserDtos = externalRestTemplate
                    .exchange(
                            backendProperties.getUrl() + "/api/account-service/users",
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<ShortUserDto>>() {})
                    .getBody();

        } catch (Throwable e) {
            model.addAttribute("externalTransferErrors", List.of("Список пользователей недоступен"));
        }
        model.addAttribute("users", shortUserDtos);

        model.addAttribute("ratesUrl", backendProperties.getUrl() + "/api/exchange-service/rates");

        return "main";
    }

    @GetMapping("/")
    public String redirectToMainPage() {
        return "redirect:/main";
    }


    @PostMapping("/user/editPassword")
    public String changeUserPassword(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                     Model model,
                                     @ModelAttribute UpdateUserPasswordForm updateUserPasswordForm,
                                     RedirectAttributes redirectAttributes) {

        String password = updateUserPasswordForm.getPassword();
        String confirmPassword = updateUserPasswordForm.getConfirmPassword();

        if (!password.equals(confirmPassword)) {
            model.addAttribute("errors", "Пароль и подтверждение на совпадают");
            redirectAttributes.addFlashAttribute("passwordErrors", "Пароль и подтверждение на совпадают");
            return "redirect:/main";
        }

        String passwordHash = passwordEncoder.encode(password);
        UpdateUserPasswordDto updateUserPasswordDto = new UpdateUserPasswordDto(passwordHash);

        try {
            externalRestTemplate.put(
                    backendProperties.getUrl() + "/api/account-service/users/" + appUserDetails.getLogin() + "/password",
                    updateUserPasswordDto
            );
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
            externalRestTemplate.put(backendProperties.getUrl() + "/api/account-service/users/" + appUserDetails.getLogin(), updateUserDto);

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
                                      @ModelAttribute AccountsStateChangeForm accountsStateChangeForm,
                                      RedirectAttributes redirectAttributes) {

        List<AccountsChangeRequestDto> accountsChangeRequestDto = accountsStateChangeForm
                .getAccounts()
                .stream()
                .map(q -> new AccountsChangeRequestDto(q.isActive(), q.getCurrency()))
                .toList();

        try {
            List<AccountDto> accountDtos = externalRestTemplate.exchange(
                    backendProperties.getUrl() + "/api/account-service/accounts/change-requests/" + appUserDetails.getLogin(),
                    HttpMethod.POST,
                    new HttpEntity<>(accountsChangeRequestDto),
                    new ParameterizedTypeReference<List<AccountDto>>() {}
            ).getBody();

        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            redirectAttributes.addFlashAttribute("userAccountsErrors", List.of(apiErrorDto.getMessage()));
        }

        return "redirect:/main";
    }

    @PostMapping("/transaction/cash-transaction")
    public String createCashTransaction(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                      Model model,
                                      @ModelAttribute CreateCashTransactionDto createCashTransactionDto,
                                      RedirectAttributes redirectAttributes) {

        createCashTransactionDto.setUserLogin(appUserDetails.getLogin());

        try {
            externalRestTemplate
                    .postForEntity(backendProperties.getUrl() + "/api/cash-service/cash-transactions", createCashTransactionDto, Void.class);
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            redirectAttributes.addFlashAttribute("cashErrors", List.of(apiErrorDto.getMessage()));
        } catch (Throwable e) {
            redirectAttributes.addFlashAttribute("cashErrors", List.of("Сервис недоступен"));
        }

        return "redirect:/main";
    }

    @PostMapping("/transaction/external-transfer-transaction")
    public String createSelfTransferTransaction(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                         Model model,
                                         @ModelAttribute CreateTransferTransactionDto createTransferTransactionDto,
                                         RedirectAttributes redirectAttributes) {

        createTransferTransactionDto.setFromLogin(appUserDetails.getLogin());
        createTransferTransactionDto.setToLogin(appUserDetails.getLogin());

        try {
            externalRestTemplate
                    .postForEntity(backendProperties.getUrl() + "/api/transfer-service/transfer-transactions/self-transactions",
                            createTransferTransactionDto, Void.class);
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            redirectAttributes.addFlashAttribute("selfTransferErrors", List.of(apiErrorDto.getMessage()));
        } catch (Throwable e) {
            redirectAttributes.addFlashAttribute("selfTransferErrors", List.of("Сервис недоступен"));
        }

        return "redirect:/main";
    }

    @PostMapping("/transaction/self-transfer-transaction")
    public String createExternalTransferTransaction(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                                 Model model,
                                                 @ModelAttribute CreateTransferTransactionDto creatTransferTransactionDto,
                                                 RedirectAttributes redirectAttributes) {

        creatTransferTransactionDto.setFromLogin(appUserDetails.getLogin());

        try {
            externalRestTemplate
                    .postForEntity(backendProperties.getUrl() + "/api/transfer-service/transfer-transactions/external-transactions",
                            creatTransferTransactionDto, Void.class);
        } catch (HttpClientErrorException e) {
            ApiErrorDto apiErrorDto = e.getResponseBodyAs(ApiErrorDto.class);
            redirectAttributes.addFlashAttribute("externalTransferErrors", List.of(apiErrorDto.getMessage()));
        } catch (Throwable e) {
            redirectAttributes.addFlashAttribute("externalTransferErrors", List.of("Сервис недоступен"));
        }

        return "redirect:/main";
    }

}

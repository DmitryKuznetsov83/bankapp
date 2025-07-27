package ru.yandex.practicum.bank.cash;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bank.cash.dto.CashTransactionDto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.yandex.practicum.bank.cash.enums.CashTransactionType.*;
import static ru.yandex.practicum.bank.cash.enums.Currency.*;
import static ru.yandex.practicum.bank.cash.enums.TransactionStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(
        ids = "ru.yandex.practicum:blocker-service:+:stubs:8080",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
public class BlockerServiceContractTest {

    @Autowired
    private StubFinder stubFinder;

    @Test
    void getData_shouldReturnMessageFromB() {
        CashTransactionDto testTransaction = CashTransactionDto.builder()
                .id(UUID.randomUUID())
                .status(PENDING)
                .userLogin("TestUser")
                .currency(USD)
                .type(CASH_IN)
                .sum(new BigDecimal("100"))
                .build();

        String blockerServiceUrl = stubFinder.findStubUrl("blocker-service").toString();

        ResponseEntity<Map<String, Boolean>> response = new RestTemplate().exchange(
                blockerServiceUrl + "/blockers/cash-transactions/validate",
                HttpMethod.POST,
                new HttpEntity<>(testTransaction),
                new ParameterizedTypeReference<Map<String, Boolean>>() {}
        );
        Boolean isValid = response.getBody().get("valid");
        assertTrue(isValid);
    }

}

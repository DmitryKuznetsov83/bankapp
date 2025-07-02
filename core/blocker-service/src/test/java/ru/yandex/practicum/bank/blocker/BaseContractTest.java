package ru.yandex.practicum.bank.blocker;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.bank.blocker.dto.CashTransactionDto;
import ru.yandex.practicum.bank.blocker.service.BlockerService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest
public abstract class BaseContractTest {

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.mockMvc(
                MockMvcBuilders.webAppContextSetup(context).build()
        );
        when(blockerService.isValidTransaction(any(CashTransactionDto.class)))
                .thenReturn(true);
    }

    @MockitoBean
    protected BlockerService blockerService;

}

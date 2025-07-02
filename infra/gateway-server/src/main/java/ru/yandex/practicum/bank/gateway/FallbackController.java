package ru.yandex.practicum.bank.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallbacks")
public class FallbackController {
    @GetMapping("blocker")
    Boolean blockerServiceFallback() {
        return false;
    }
}
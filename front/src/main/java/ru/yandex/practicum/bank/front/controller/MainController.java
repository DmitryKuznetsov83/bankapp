package ru.yandex.practicum.bank.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/main")
    public String getMainPage() {
        return "main";
    }

    @GetMapping("/")
    public String redirectToMainPage() {
        return "redirect:/main";
    }

}

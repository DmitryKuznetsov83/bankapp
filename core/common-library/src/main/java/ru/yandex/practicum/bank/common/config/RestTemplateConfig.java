package ru.yandex.practicum.bank.common.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate internalRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate externalRestTemplate() {
        RestTemplate template = new RestTemplate();
        template.setInterceptors(Collections.emptyList());
        return template;
    }

}

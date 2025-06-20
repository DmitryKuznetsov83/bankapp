package ru.yandex.practicum.bank.common.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LoadBalancedClientConfig {

    @Bean
    @LoadBalanced
    public RestTemplate internalRestTemplate() {
        return new RestTemplate();
    }

}

package ru.yandex.practicum.bank.front.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bank.front.dto.user.UserDto;
import ru.yandex.practicum.bank.front.mapper.UserMapper;

public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try {
            UserDto userDto = restTemplate
                    .getForObject("http://bankapp-account-service:8080/users/" + login, UserDto.class);
            return UserMapper.INSTANCE.toAppUser(userDto);
        } catch (HttpClientErrorException.NotFound exception) {
            log.warn("User with login " + login + " not found", exception);
            throw new UsernameNotFoundException(login);
        }
    }

}

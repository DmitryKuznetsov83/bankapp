package ru.yandex.practicum.bank.front.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.bank.front.config.BackendProperties;
import ru.yandex.practicum.bank.front.dto.user.UserDto;
import ru.yandex.practicum.bank.front.mapper.UserMapper;

public class UserDetailsServiceImpl implements UserDetailsService {

    private final RestTemplate externalRestTemplate;
    private final BackendProperties backendProperties;

    public UserDetailsServiceImpl(RestTemplate externalRestTemplate, BackendProperties backendProperties) {
        this.externalRestTemplate = externalRestTemplate;
        this.backendProperties = backendProperties;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try {
            UserDto userDto = externalRestTemplate
                    .getForObject(backendProperties.getUrl() + "/api/account-service/users/" + login, UserDto.class);
            return UserMapper.INSTANCE.toAppUser(userDto);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new UsernameNotFoundException(login);
        }
    }

}

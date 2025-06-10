package ru.yandex.practicum.bank.front.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.bank.front.dto.UserDto;
import ru.yandex.practicum.bank.front.mapper.UserMapper;

public class UserDetailsServiceImpl implements UserDetailsService {

    private final RestClient restClient = RestClient.create();

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try {
            UserDto userDto = restClient
                    .get()
                    .uri("http://localhost:8082/users/" + login)
                    .retrieve()
                    .body(UserDto.class);
            return UserMapper.INSTANCE.toAppUser(userDto);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new UsernameNotFoundException(login);
        }
    }


}

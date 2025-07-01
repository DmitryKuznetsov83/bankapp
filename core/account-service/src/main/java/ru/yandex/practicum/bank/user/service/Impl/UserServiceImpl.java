package ru.yandex.practicum.bank.user.service.Impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.bank.user.dto.user.*;
import ru.yandex.practicum.bank.user.exception.user.LoginAlreadyUsedException;
import ru.yandex.practicum.bank.user.exception.user.UserNotFoundException;
import ru.yandex.practicum.bank.user.mapper.UserMapper;
import ru.yandex.practicum.bank.user.model.User;
import ru.yandex.practicum.bank.user.repository.UserJpaRepository;
import ru.yandex.practicum.bank.user.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserJpaRepository userJpaRepository;

    public UserServiceImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    @Transactional
    public UserDto creatUser(UserDto userDto) {
        String login = userDto.getLogin();

        if (userJpaRepository.existsByLogin(login)) {
            throw new LoginAlreadyUsedException(login);
        }

        User newUser = UserMapper.INSTANCE.toUser(userDto);
        try {
            User savedUser = userJpaRepository.save(newUser);
            return UserMapper.INSTANCE.toUserDto(savedUser);
        } catch (DataIntegrityViolationException e) {
            if (isDuplicateLoginError(e)) {
                throw new LoginAlreadyUsedException(userDto.getLogin());
            }
            throw e;
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(String login, UpdateUserDto updateUserDto) {
        User user = userJpaRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
        user.setName(updateUserDto.getName());
        user.setBirthdate(updateUserDto.getBirthdate());
        User savedUser = userJpaRepository.save(user);
        return UserMapper.INSTANCE.toUserDto(savedUser);
    }

    @Override
    @Transactional
    public void updateUserPassword(String login, UpdateUserPasswordDto updateUserPasswordDto) {
        User user = userJpaRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));

        user.setPasswordHash(updateUserPasswordDto.getPasswordHash());
        userJpaRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(String login) {
        return UserMapper.INSTANCE.toUserDto(userJpaRepository
                .findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortUserDto> getUsers() {
        return userJpaRepository.findAll()
                .stream()
                .map(UserMapper.INSTANCE::toShortUserDto)
                .toList();
    }

    private boolean isDuplicateLoginError(DataIntegrityViolationException e) {
        return e.getMessage() != null && e.getMessage().toLowerCase().contains("login");
    }

}

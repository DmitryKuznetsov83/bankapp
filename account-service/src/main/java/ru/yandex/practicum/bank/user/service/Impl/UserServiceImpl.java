package ru.yandex.practicum.bank.user.service.Impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.bank.user.dto.user.CreateUserDto;
import ru.yandex.practicum.bank.user.dto.user.UpdateUserDto;
import ru.yandex.practicum.bank.user.dto.user.UpdateUserPasswordDto;
import ru.yandex.practicum.bank.user.dto.user.UserDto;
import ru.yandex.practicum.bank.user.exception.user.LoginAlreadyUsedException;
import ru.yandex.practicum.bank.user.exception.user.PasswordAndConfirmationDoNotMatchException;
import ru.yandex.practicum.bank.user.exception.user.PasswordIsSameAsPreviousException;
import ru.yandex.practicum.bank.user.exception.user.UserAccountNotFoundException;
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
    public UserDto creatUser(CreateUserDto createUserDto) {
        String login = createUserDto.getLogin();

        if (userJpaRepository.existsByLogin(login)) {
            throw new LoginAlreadyUsedException(login);
        }
        if (!createUserDto.getPassword().equals(createUserDto.getConfirmPassword())) {
            throw new PasswordAndConfirmationDoNotMatchException(login);
        }

        User newUser = UserMapper.INSTANCE.toUser(createUserDto);
        try {
            User savedUser = userJpaRepository.save(newUser);
            return UserMapper.INSTANCE.toUserDto(savedUser);
        } catch (DataIntegrityViolationException e) {
            if (isDuplicateLoginError(e)) {
                throw new LoginAlreadyUsedException(createUserDto.getLogin());
            }
            throw e;
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(String login, UpdateUserDto updateUserDto) {
        User user = userJpaRepository.findByLogin(login)
                .orElseThrow(() -> new UserAccountNotFoundException(login));
        user.setName(updateUserDto.getName());
        user.setBirthdate(updateUserDto.getBirthdate());
        User savedUser = userJpaRepository.save(user);
        return UserMapper.INSTANCE.toUserDto(savedUser);
    }

    @Override
    @Transactional
    public void updateUserPassword(String login, UpdateUserPasswordDto updateUserPasswordDto) {
        if (!updateUserPasswordDto.getPassword().equals(updateUserPasswordDto.getConfirmPassword())) {
            throw new PasswordAndConfirmationDoNotMatchException(login);
        }
        User user = userJpaRepository.findByLogin(login)
                .orElseThrow(() -> new UserAccountNotFoundException(login));
        if (user.getPasswordHash().equals(updateUserPasswordDto.getPassword())) {
            throw new PasswordIsSameAsPreviousException();
        }

        user.setPasswordHash(updateUserPasswordDto.getPassword());
        userJpaRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(String login) {
        User user = userJpaRepository.findByLogin(login)
                .orElseThrow(() -> new UserAccountNotFoundException(login));
        userJpaRepository.deleteById(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(String login) {
        return UserMapper.INSTANCE.toUserDto(userJpaRepository
                .findByLogin(login)
                .orElseThrow(() -> new UserAccountNotFoundException(login)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getUsers() {
        return userJpaRepository.findAll()
                .stream()
                .map(User::getLogin)
                .toList();
    }

    private boolean isDuplicateLoginError(DataIntegrityViolationException e) {
        return e.getMessage() != null && e.getMessage().toLowerCase().contains("login");
    }

}

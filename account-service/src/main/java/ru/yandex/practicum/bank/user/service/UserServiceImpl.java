package ru.yandex.practicum.bank.user.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.bank.user.dto.CreateUserDto;
import ru.yandex.practicum.bank.user.dto.UpdateUserDto;
import ru.yandex.practicum.bank.user.dto.UpdateUserPasswordDto;
import ru.yandex.practicum.bank.user.dto.UserDto;
import ru.yandex.practicum.bank.user.exception.LoginAlreadyUsedException;
import ru.yandex.practicum.bank.user.exception.PasswordAndConfirmationDoNotMatchException;
import ru.yandex.practicum.bank.user.exception.UserAccountNotFoundException;
import ru.yandex.practicum.bank.user.mapper.UserMapper;
import ru.yandex.practicum.bank.user.model.User;
import ru.yandex.practicum.bank.user.repository.UserJpaRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

//    private final int AGE_OF_MAJORITY = 18;

    private final UserJpaRepository userJpaRepository;

    public UserServiceImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    @Transactional
    public UserDto creatUser(CreateUserDto createUserDto) {
        String login = createUserDto.getLogin();

//        if (!userIsOfLegalAge(createUserDto.getBirthdate())) {
//            throw new UserNotIsOfLegalAgeException(login);
//        }
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
//        if (!userIsOfLegalAge(updateUserDto.getBirthdate())) {
//            throw new UserNotIsOfLegalAgeException(login);
//        }
        User user = userJpaRepository.findByLogin(login)
                .orElseThrow(() -> new UserAccountNotFoundException(login));
        user.setFullName(updateUserDto.getFullName());
        user.setBirthdate(updateUserDto.getBirthdate());
        User savedUser = userJpaRepository.save(user);
        return UserMapper.INSTANCE.toUserDto(savedUser);
    }

    @Override
    public void updateUserPassword(String login, UpdateUserPasswordDto updateUserPasswordDto) {
        if (!updateUserPasswordDto.getPassword().equals(updateUserPasswordDto.getConfirmPassword())) {
            throw new PasswordAndConfirmationDoNotMatchException(login);
        }
        User user = userJpaRepository.findByLogin(login)
                .orElseThrow(() -> new UserAccountNotFoundException(login));
        user.setPasswordHash(updateUserPasswordDto.getPassword());
        userJpaRepository.save(user);
    }

    @Override
    public void deleteUser(String login) {
        User user = userJpaRepository.findByLogin(login)
                .orElseThrow(() -> new UserAccountNotFoundException(login));
        userJpaRepository.deleteById(user.getId());
    }

    @Override
    public UserDto getUser(String login) {
        return UserMapper.INSTANCE.toUserDto(userJpaRepository
                .findByLogin(login)
                .orElseThrow(() -> new UserAccountNotFoundException(login)));
    }

    @Override
    public List<UserDto> getUsers() {
        return userJpaRepository.findAll()
                .stream()
                .map(UserMapper.INSTANCE::toUserDto)
                .toList();
    }

//    private boolean userIsOfLegalAge(LocalDate birthdate) {
//        return ChronoUnit.YEARS.between(birthdate, LocalDate.now()) >= AGE_OF_MAJORITY;
//    }

    private boolean isDuplicateLoginError(DataIntegrityViolationException e) {
        return e.getMessage() != null && e.getMessage().toLowerCase().contains("login");
    }

}

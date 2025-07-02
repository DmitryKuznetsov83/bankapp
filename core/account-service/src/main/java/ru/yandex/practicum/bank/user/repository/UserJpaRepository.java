package ru.yandex.practicum.bank.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.bank.user.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<User, UUID> {

    boolean existsByLogin(String login);

    Optional<User> findByLogin(String login);

}

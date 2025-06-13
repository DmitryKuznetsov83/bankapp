package ru.yandex.practicum.bank.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.bank.user.model.Account;
import ru.yandex.practicum.bank.user.model.User;

import java.util.List;
import java.util.UUID;

public interface AccountJpaRepository extends JpaRepository<Account, UUID>  {

    List<Account> findByOwner(User owner);

}

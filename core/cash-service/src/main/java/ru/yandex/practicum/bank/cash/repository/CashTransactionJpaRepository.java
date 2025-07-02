package ru.yandex.practicum.bank.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.bank.cash.model.CashTransaction;

import java.util.List;
import java.util.UUID;

public interface CashTransactionJpaRepository extends JpaRepository<CashTransaction, UUID> {

    List<CashTransaction> findByUserLogin(String login);

}

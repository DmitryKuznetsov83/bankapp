package ru.yandex.practicum.bank.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.bank.transfer.model.TransferTransaction;

import java.util.List;
import java.util.UUID;

public interface TransferTransactionJpaRepository extends JpaRepository<TransferTransaction, UUID> {

    List<TransferTransaction> findByFromLoginOrToLogin(String fromLogin, String toLogin);

}

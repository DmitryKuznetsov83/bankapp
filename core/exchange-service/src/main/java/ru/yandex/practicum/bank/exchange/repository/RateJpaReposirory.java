package ru.yandex.practicum.bank.exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.bank.exchange.model.CurrencyRate;

import java.util.List;
import java.util.UUID;

public interface RateJpaReposirory extends JpaRepository<CurrencyRate, UUID> {

    @Query("SELECT r FROM CurrencyRate r WHERE r.timestamp = (SELECT MAX(r2.timestamp) FROM CurrencyRate r2 WHERE r2.currency = r.currency)")
    List<CurrencyRate> findLatestRates();

}

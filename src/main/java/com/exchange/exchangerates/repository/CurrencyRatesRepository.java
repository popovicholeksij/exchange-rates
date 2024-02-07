package com.exchange.exchangerates.repository;

import com.exchange.exchangerates.entity.ExchangeRateEntity;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CurrencyRatesRepository extends JpaRepository<ExchangeRateEntity, Integer> {

    @Caching
    @Query("select a from ExchangeRateEntity a where a.exchangeDate = :exchangeDate")
    List<ExchangeRateEntity> findByExchangeDate(@Param("exchangeDate") LocalDate exchangeDate);

    @Modifying
    @Transactional
    @Query("delete from ExchangeRateEntity where exchangeDate = :exchangeDate")
    void deleteByExchangeDate(@Param("exchangeDate") LocalDate date);
}

package com.exchange.exchangerates.service.impl;

import com.exchange.exchangerates.client.ExchangeRatesRestClient;
import com.exchange.exchangerates.dto.ExchangeRate;
import com.exchange.exchangerates.dto.ExchangeRateResponse;
import com.exchange.exchangerates.entity.ExchangeRateEntity;
import com.exchange.exchangerates.mapper.ExchangeRateEntityMapper;
import com.exchange.exchangerates.repository.CurrencyRatesRepository;
import com.exchange.exchangerates.service.ExchangeRatesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Profile("dev")
@Service
public class DevExchangeRatesService implements ExchangeRatesService {

    private final ExchangeRatesRestClient client;
    private final CurrencyRatesRepository repository;
    private final ExchangeRateEntityMapper mapper;

    public DevExchangeRatesService(ExchangeRatesRestClient client,
                                   CurrencyRatesRepository repository,
                                   ExchangeRateEntityMapper mapper) {
        this.client = client;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<List<ExchangeRateResponse>> getActualExchangeRates() {
        log.info("Start of getting actual exchange rates");
        log.info("Trying to find actual exchange rates in db");
        List<ExchangeRateEntity> ratesList = repository.findByExchangeDate(LocalDate.now());
        if (!ratesList.isEmpty()) {
            log.info("Actual exchange rates found in db");
            return Optional.of(mapper.mapListEntityToResponse(ratesList));
        } else {
            log.info("Actual exchange rates not found in db");
            List<ExchangeRate> actualExchangeRates = client.getActualExchangeRates();
            repository.saveAll(mapper.mapToListEntity(actualExchangeRates));
            return Optional.of(mapper.mapListToResponse(actualExchangeRates));
        }

    }

    @Override
    public Optional<List<ExchangeRateResponse>> getExchangeRatesForDate(LocalDate date) {
        log.info("Start of getting actual exchange rates by date={}", date);
        log.info("Trying to find exchange rates in db");
        List<ExchangeRateEntity> byExchangeDate = repository.findByExchangeDate(date);
        if (byExchangeDate.isEmpty()) {
            log.info("Exchange rates not found in db");
            List<ExchangeRate> exchangeRatesByDate = client.getExchangeRatesByDate(date);
            repository.saveAll(mapper.mapToListEntity(exchangeRatesByDate));
            return Optional.of(mapper.mapListToResponse(exchangeRatesByDate));
        }
        log.info("Exchange rates found in db");
        return Optional.of(mapper.mapListEntityToResponse(byExchangeDate));
    }

    @Override
    public void deleteExchangeRatesForDate(LocalDate date) {
        log.info("Start of removing exchange rates by date={}", date);
        repository.deleteByExchangeDate(date);
    }
}

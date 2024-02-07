package com.exchange.exchangerates.service;

import com.exchange.exchangerates.dto.ExchangeRateResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExchangeRatesService {
    Optional<List<ExchangeRateResponse>> getActualExchangeRates() throws IOException;

    Optional<List<ExchangeRateResponse>> getExchangeRatesForDate(LocalDate date);

    void deleteExchangeRatesForDate(LocalDate date);
}

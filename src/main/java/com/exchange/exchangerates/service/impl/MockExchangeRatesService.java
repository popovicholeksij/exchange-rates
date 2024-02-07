package com.exchange.exchangerates.service.impl;

import com.exchange.exchangerates.dto.ExchangeRate;
import com.exchange.exchangerates.dto.ExchangeRateResponse;
import com.exchange.exchangerates.entity.ExchangeRateEntity;
import com.exchange.exchangerates.mapper.ExchangeRateEntityMapper;
import com.exchange.exchangerates.service.ExchangeRatesService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Profile("mock")
@Service
public class MockExchangeRatesService implements ExchangeRatesService {

    private static final String EXCHANGE_RATES_MOCK_1 = "mock/files/exchange_rates_2024_02_03.json";
    private static final String EXCHANGE_RATES_MOCK_2 = "mock/files/exchange_rates_2024_02_04.json";
    private static final String EXCHANGE_RATES_MOCK_3 = "mock/files/exchange_rates_2024_02_05.json";
    private static final String EXCHANGE_RATES_MOCK = "mock/files/exchange_rates_mock.json";
    private final ExchangeRateEntityMapper mapper;
    private final ObjectMapper objectMapper;
    private Map<LocalDate, List<ExchangeRateEntity>> mockMap = new HashMap<>();

    public MockExchangeRatesService(ExchangeRateEntityMapper mapper,
                                    ObjectMapper objectMapper) {
        this.mapper = mapper;
        this.objectMapper = objectMapper;
        initMockMap();
    }

    @SneakyThrows
    private void initMockMap() {
        List<String> mockFileNamesList = List.of(EXCHANGE_RATES_MOCK_1, EXCHANGE_RATES_MOCK_2, EXCHANGE_RATES_MOCK_3);
        for (String mockFileName : mockFileNamesList) {
            List<ExchangeRateEntity> ratesList = objectMapper.readValue(
                    new ClassPathResource(mockFileName).getInputStream(),
                    new TypeReference<>() {
                    });
            mockMap.put(ratesList.get(0).getExchangeDate(), ratesList);
        }
    }

    @Override
    public Optional<List<ExchangeRateResponse>> getActualExchangeRates() throws IOException {
        log.info("(MockExchangeRatesService) Start of getting actual exchange rates");
        List<ExchangeRate> ratesList = objectMapper.readValue(
                new ClassPathResource(EXCHANGE_RATES_MOCK).getInputStream(),
                new TypeReference<>() {
                });
        return Optional.of(mapper.mapListToResponse(ratesList));
    }

    @Override
    public Optional<List<ExchangeRateResponse>> getExchangeRatesForDate(LocalDate date) {
        log.info("(MockExchangeRatesService) Start of getting exchange rates by date={}", date);
        List<ExchangeRateEntity> exchangeRates = mockMap.get(date);
        if (exchangeRates.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapper.mapListEntityToResponse(exchangeRates));
    }

    @Override
    public void deleteExchangeRatesForDate(LocalDate date) {
        log.info("(MockExchangeRatesService) Start of removing exchange rates by date={}", date);
    }
}

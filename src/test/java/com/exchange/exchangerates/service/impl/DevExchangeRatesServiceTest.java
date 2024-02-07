package com.exchange.exchangerates.service.impl;

import com.exchange.exchangerates.client.ExchangeRatesRestClient;
import com.exchange.exchangerates.dto.ExchangeRate;
import com.exchange.exchangerates.dto.ExchangeRateResponse;
import com.exchange.exchangerates.entity.ExchangeRateEntity;
import com.exchange.exchangerates.exceptions.ApiException;
import com.exchange.exchangerates.mapper.ExchangeRateEntityMapper;
import com.exchange.exchangerates.repository.CurrencyRatesRepository;
import com.exchange.exchangerates.service.ExchangeRatesService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DevExchangeRatesServiceTest {

    @Autowired
    private ExchangeRateEntityMapper mapper;
    private ObjectMapper objectMapper;
    private ExchangeRatesService service;
    private ExchangeRatesRestClient client;
    private CurrencyRatesRepository repository;
    private LocalDate date;


    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .findAndRegisterModules();
        client = Mockito.mock(ExchangeRatesRestClient.class);
        repository = Mockito.mock(CurrencyRatesRepository.class);
        service = new DevExchangeRatesService(client, repository, mapper);
        date = LocalDate.of(2024, 2, 3);
    }

    @Test
    void getActualExchangeRatesExistsInDB() throws IOException {
        List<ExchangeRateEntity> ratesList = objectMapper.readValue(
                new ClassPathResource("json/exchange_rates_entity_list.json").getInputStream(),
                new TypeReference<>() {
                });
        when(repository.findByExchangeDate(any(LocalDate.class))).thenReturn(ratesList);

        Optional<List<ExchangeRateResponse>> actualExchangeRates = service.getActualExchangeRates();

        verify(repository, times(1)).findByExchangeDate(any(LocalDate.class));
        assertTrue(actualExchangeRates.isPresent());
        assertEquals(ratesList.size(), actualExchangeRates.get().size());
    }

    @Test
    void getActualExchangeRatesNotExistsInDB() throws IOException {
        List<ExchangeRate> ratesList = objectMapper.readValue(
                new ClassPathResource("json/exchange_rates_response_list.json").getInputStream(),
                new TypeReference<>() {
                });
        when(repository.findByExchangeDate(any(LocalDate.class))).thenReturn(Collections.emptyList());
        when(client.getActualExchangeRates()).thenReturn(ratesList);

        Optional<List<ExchangeRateResponse>> actualExchangeRates = service.getActualExchangeRates();

        verify(repository, times(1)).findByExchangeDate(any(LocalDate.class));
        verify(repository, times(1)).saveAll(any());
        verify(client, times(1)).getActualExchangeRates();
        assertTrue(actualExchangeRates.isPresent());
        assertEquals(ratesList.size(), actualExchangeRates.get().size());
    }

    @Test
    void getActualExchangeRatesThrowClientException() {
        when(repository.findByExchangeDate(any(LocalDate.class))).thenReturn(Collections.emptyList());
        when(client.getActualExchangeRates())
                .thenThrow(new ApiException(HttpStatusCode.valueOf(500), "test error"));

        Exception exception = assertThrows(ApiException.class, () -> service.getActualExchangeRates());

        assertEquals("test error", exception.getMessage());
    }

    @Test
    void getExchangeRatesForDateNotExistsInDB() throws IOException {
        List<ExchangeRate> ratesList = objectMapper.readValue(
                new ClassPathResource("json/exchange_rates_response_list.json").getInputStream(),
                new TypeReference<>() {
                });
        when(repository.findByExchangeDate(date)).thenReturn(Collections.emptyList());
        when(client.getExchangeRatesByDate(date)).thenReturn(ratesList);

        Optional<List<ExchangeRateResponse>> actualExchangeRates = service.getExchangeRatesForDate(date);

        verify(repository, times(1)).findByExchangeDate(date);
        verify(client, times(1)).getExchangeRatesByDate(date);
        verify(repository, times(1)).saveAll(any());
        assertTrue(actualExchangeRates.isPresent());
        assertEquals(ratesList.size(), actualExchangeRates.get().size());
    }

    @Test
    void getExchangeRatesForDateExistsInDB() throws IOException {
        List<ExchangeRateEntity> ratesList = objectMapper.readValue(
                new ClassPathResource("json/exchange_rates_entity_list.json").getInputStream(),
                new TypeReference<>() {
                });
        when(repository.findByExchangeDate(date)).thenReturn(ratesList);

        Optional<List<ExchangeRateResponse>> actualExchangeRates = service.getExchangeRatesForDate(date);

        verify(repository, times(1)).findByExchangeDate(date);
        assertTrue(actualExchangeRates.isPresent());
        assertEquals(ratesList.size(), actualExchangeRates.get().size());
    }

    @Test
    void deleteExchangeRatesForDate() {
        doNothing().when(repository).deleteByExchangeDate(date);
        service.deleteExchangeRatesForDate(date);
        verify(repository, times(1)).deleteByExchangeDate(date);
    }
}
package com.exchange.exchangerates.controller;

import com.exchange.exchangerates.dto.ExchangeRateResponse;
import com.exchange.exchangerates.service.ExchangeRatesService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/exchange-rates")
public class ExchangeRatesController {

    private final ExchangeRatesService service;

    public ExchangeRatesController(ExchangeRatesService service) {
        this.service = service;
    }

    @SneakyThrows
    @GetMapping
    ResponseEntity<List<ExchangeRateResponse>> getExchangeRates(@RequestParam(value = "date", required = false) String date) {
        if (date == null) {
            return ResponseEntity.of(service.getActualExchangeRates());
        }
        return ResponseEntity.of(service.getExchangeRatesForDate(LocalDate.parse(date)));
    }

    @DeleteMapping
    ResponseEntity<Void> deleteExchangeRatesForDate(@RequestParam("date") String date) {
        service.deleteExchangeRatesForDate(LocalDate.parse(date));
        return ResponseEntity.ok().build();
    }
}

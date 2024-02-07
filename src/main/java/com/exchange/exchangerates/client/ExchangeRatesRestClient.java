package com.exchange.exchangerates.client;

import com.exchange.exchangerates.dto.ExchangeRate;
import com.exchange.exchangerates.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class ExchangeRatesRestClient {

    private final RestTemplate restTemplate;
    private final String exchangeRatesUrl;

    public ExchangeRatesRestClient(RestTemplate restTemplate,
                                   @Value("${nbu.exchange.rates.url}") String exchangeRatesUrl) {
        this.restTemplate = restTemplate;
        this.exchangeRatesUrl = exchangeRatesUrl;
    }

    public List<ExchangeRate> getActualExchangeRates() {
        log.info("Getting actual exchange rates by url={}", exchangeRatesUrl);
        List<ExchangeRate> response = getResponse(exchangeRatesUrl);
        log.info("Received {} exchange rates", response.size());
        return response;
    }

    public List<ExchangeRate> getExchangeRatesByDate(LocalDate date) {
        String url = exchangeRatesUrl + "&date=" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("Getting exchange rates for date={} by url={}", date, url);
        List<ExchangeRate> response = getResponse(url);
        log.info("Received {} exchange rates", response.size());
        return response;
    }

    private List<ExchangeRate> getResponse(String url) {
        List<ExchangeRate> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExchangeRate>>() {
                }).getBody();
        if (response == null) {
            log.error("Exchange rates by url= {} not found", url);
            throw new ApiException(HttpStatusCode.valueOf(500), "Unable to get exchange rates from remote server");
        }
        return response;
    }
}

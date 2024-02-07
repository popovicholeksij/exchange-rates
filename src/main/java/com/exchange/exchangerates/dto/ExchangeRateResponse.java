package com.exchange.exchangerates.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
public class ExchangeRateResponse {

    @JsonProperty("currency_id")
    private Integer currencyId;

    private String name;

    private String code;

    private BigDecimal rate;

    @JsonProperty("exchange_date")
    private String exchangeDate;

    @JsonProperty("last_updated")
    private String lastUpdated;
}

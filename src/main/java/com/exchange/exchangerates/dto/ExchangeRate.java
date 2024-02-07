package com.exchange.exchangerates.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
public class ExchangeRate {

    @JsonProperty("r030")
    private Integer id;

    @JsonProperty("txt")
    private  String name;

    @JsonProperty("cc")
    private String code;

    @JsonProperty("rate")
    private BigDecimal rate;

    @JsonProperty("exchangedate")
    private String exchangeDate;

    @JsonProperty("last_updated")
    private String lastUpdated;
}

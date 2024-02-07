package com.exchange.exchangerates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "exchange_rates")
public class ExchangeRateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "currency_id")
    private Integer currencyId;

    private String name;

    private String code;

    private BigDecimal rate;

    @Column(name = "exchange_date")
    private LocalDate exchangeDate;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

}

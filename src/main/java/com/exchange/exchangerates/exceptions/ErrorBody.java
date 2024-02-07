package com.exchange.exchangerates.exceptions;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class ErrorBody implements Serializable {

    private static final long serialVersionUID = 1L;

    private String timestamp;
    private Integer status;
    private String error;

    @Builder
    public ErrorBody(Integer status, String error) {
        this.timestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now());
        this.status = status;
        this.error = error;
    }
}

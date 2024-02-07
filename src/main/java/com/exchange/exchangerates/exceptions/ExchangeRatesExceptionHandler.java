package com.exchange.exchangerates.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class ExchangeRatesExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                 HttpHeaders headers,
                                                                 HttpStatusCode status,
                                                                 WebRequest request) {
        return toResponseEntity(ex, status, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        return toResponseEntity(ex, status, ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity handleIoException(IOException ex) {
        return toResponseEntity(ex, HttpStatusCode.valueOf(400), ex.getMessage());
    }

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity handleApiException(ApiException ex) {
        return toResponseEntity(ex, ex.getHttpStatusCode(), ex.getMessage());
    }

    private ResponseEntity toResponseEntity(Exception ex, HttpStatusCode status, String exceptionMessage) {
        log.error("ExchangeRatesExceptionHandler : " + exceptionMessage, ex);
        return ResponseEntity.status(status)
                .body(ErrorBody.builder()
                        .status(status.value())
                        .error(exceptionMessage)
                        .build());
    }
}

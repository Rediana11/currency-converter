package com.currency.converter.util;

import com.currency.converter.exception.InvalidAmountException;
import com.currency.converter.exception.InvalidCurrencyException;
import com.currency.converter.model.ExchangeRateRequest;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
public class InputValidator {

    private final static String CURRENCY_REGEX = "[A-Z]{3}";

    private final CircuitBreaker circuitBreaker;

    public InputValidator() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .permittedNumberOfCallsInHalfOpenState(2)
                .slidingWindowSize(2)
                .recordExceptions(InvalidCurrencyException.class, InvalidAmountException.class)
                .build();

        this.circuitBreaker = CircuitBreaker.of("currencyValidation", circuitBreakerConfig);
    }

    private void validateCurrencyCode(String currencyValue) {
        try {
            circuitBreaker.executeSupplier(() -> {
                if (currencyValue == null || !currencyValue.matches(CURRENCY_REGEX)) {
                    throw new InvalidCurrencyException("Invalid currency value");
                }
                return null;
            });
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    private void validateAmount(double amount) {
        try {
            circuitBreaker.executeSupplier(() -> {
                if (amount <= 0) {
                    throw new InvalidAmountException("Amount must be greater than zero");
                }
                return null;
            });
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    private void validateDate(String date) {
        try {
            circuitBreaker.executeSupplier(() -> {
                if (!isValidDateFormat(date)) {
                    throw new IllegalArgumentException("Date is not in the excepted format");
                }
                return null;
            });
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isValidDateFormat(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the format based on your requirements
            sdf.setLenient(false);
            Date parsedDate = sdf.parse(date);
            return parsedDate != null;
        } catch (ParseException e) {
            return false;
        }
    }

    public void validateInputs(ExchangeRateRequest exchangeRateRequest) {
        validateAmount(exchangeRateRequest.getAmount());
        validateCurrencyCode(exchangeRateRequest.getFrom());
        validateCurrencyCode(exchangeRateRequest.getTo());
        validateDate(exchangeRateRequest.getDate());
    }

}

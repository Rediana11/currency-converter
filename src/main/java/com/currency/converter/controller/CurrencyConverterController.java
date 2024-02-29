package com.currency.converter.controller;

import com.currency.converter.model.ExchangeRateRequest;
import com.currency.converter.model.ExchangeRateResponse;
import com.currency.converter.service.ExchangeRateService;
import com.currency.converter.util.InputValidator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(CurrencyConverterURL.BASE)
public class CurrencyConverterController {

    private ExchangeRateService exchangeRateService;

    private InputValidator inputValidator;

    public CurrencyConverterController(ExchangeRateService exchangeRateService, InputValidator inputValidator) {
        this.exchangeRateService = exchangeRateService;
        this.inputValidator = inputValidator;
    }

    @PostMapping(CurrencyConverterURL.CONVERT)
    public Mono<ExchangeRateResponse> convertCurrency(
            @RequestBody ExchangeRateRequest exchangeRateRequest) {

        inputValidator.validateInputs(exchangeRateRequest);
        return exchangeRateService.convertCurrency(exchangeRateRequest);
    }

}

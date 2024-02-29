package com.currency.converter.service;

import com.currency.converter.model.ExchangeRateRequest;
import com.currency.converter.model.ExchangeRateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@CacheConfig
public class ExchangeRateService {

    @Value("${external-currency-layer-api.access-key}")
    private String convertApiAccessKey;

    private final WebClient webClient;

    public ExchangeRateService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://api.currencylayer.com").build();
    }

    @Cacheable(value = "rates", key = "#exchangeRateRequest.date")
    public Mono<ExchangeRateResponse> convertCurrency(ExchangeRateRequest exchangeRateRequest) {

        log.info("The external api is invoked to get the exchange rates...");

        return webClient.get()
                .uri(createWebClientUri(exchangeRateRequest))
                .retrieve()
                .bodyToMono(ExchangeRateResponse.class);
    }

    private String createWebClientUri(ExchangeRateRequest exchangeRateRequest) {
        return "/convert?access_key=" + convertApiAccessKey + "&from=" + exchangeRateRequest.getFrom() + "&to="
                + exchangeRateRequest.getTo() + "&amount=" + exchangeRateRequest.getAmount() + "&date=" + exchangeRateRequest.getDate();
    }

}
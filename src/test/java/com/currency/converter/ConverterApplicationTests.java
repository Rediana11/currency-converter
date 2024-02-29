package com.currency.converter;
import com.currency.converter.model.ExchangeRateRequest;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import com.currency.converter.model.ExchangeRateResponse;
import com.currency.converter.model.Info;
import com.currency.converter.model.Query;
import com.currency.converter.service.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class ConverterApplicationTests {

	@Mock
	private ExchangeRateService exchangeRateService;

	@Mock
	private CircuitBreaker circuitBreaker;

	@Mock
	private CircuitBreakerRegistry circuitBreakerRegistry;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		when(circuitBreakerRegistry.circuitBreaker(anyString())).thenReturn(circuitBreaker);
	}

	@Test
	void convertCurrencyTest() {
		ExchangeRateRequest dummyRequest = createDummyRequest();
		ExchangeRateResponse dummyResponse = createDummyResponse();


		when(exchangeRateService.convertCurrency(eq(dummyRequest)))
				.thenReturn(Mono.just(dummyResponse));

		WebTestClient
				.bindToServer()
				.baseUrl("http://api.currencylayer.com")
				.build()
				.post()
				.uri(createWebClientUri(dummyRequest))
				.exchange()
				.expectBody(ExchangeRateResponse.class).isEqualTo(dummyResponse);
	}

	ExchangeRateResponse createDummyResponse() {
		ExchangeRateResponse dummyResponse = new ExchangeRateResponse();
		Info info = new Info();
		Query query = new Query();
		query.setAmount(10);
		query.setFrom("USD");
		query.setTo("EUR");
		info.setQuote(0.92301);
		info.setTimestamp(1709150223);
		dummyResponse.setSuccess(true);
		dummyResponse.setPrivacy("https://currencylayer.com/privacy");
		dummyResponse.setTerms("https://currencylayer.com/terms");
		dummyResponse.setResult(9.2301);
		dummyResponse.setInfo(info);
		dummyResponse.setQuery(query);
		return dummyResponse;
	}

	ExchangeRateRequest createDummyRequest() {
		ExchangeRateRequest dummyResponse = new ExchangeRateRequest();
		dummyResponse.setDate("2024-02-29");
		dummyResponse.setAmount(10);
		dummyResponse.setFrom("USD");
		dummyResponse.setTo("EUR");
		return dummyResponse;
	}

	private String createWebClientUri(ExchangeRateRequest exchangeRateRequest) {
		return "/convert?access_key=2ea82a1819acbec7b5e15a174479d4ce&from=" + exchangeRateRequest.getFrom() + "&to="
				+ exchangeRateRequest.getTo() + "&amount=" + exchangeRateRequest.getAmount() + "&date=" + exchangeRateRequest.getDate();
	}

}

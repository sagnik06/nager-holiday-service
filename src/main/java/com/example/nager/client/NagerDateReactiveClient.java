
package com.example.nager.client;

import com.example.nager.model.PublicHoliday;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Component
public class NagerDateReactiveClient {
    private static final Logger log = LoggerFactory.getLogger(NagerDateReactiveClient.class);
    private final WebClient webClient;
    private final RateLimiterRegistry rateLimiterRegistry;

    @Value("${app.webclient.retry.max-retries:2}") private int maxRetries;
    @Value("${app.webclient.retry.backoff-ms:300}") private long backoffMs;
    @Value("${app.webclient.retry.jitter:0.2}") private double jitter;

    public NagerDateReactiveClient(WebClient webClient, RateLimiterRegistry rateLimiterRegistry) {
        this.webClient = webClient;
        this.rateLimiterRegistry = rateLimiterRegistry;
    }

    public Mono<List<PublicHoliday>> getPublicHolidays(int year, String countryCode) {
        String path = "/PublicHolidays/" + year + "/" + countryCode;

        Retry retry = Retry.backoff(maxRetries, Duration.ofMillis(backoffMs))
                .jitter(jitter)
                .filter(this::isRetriable)
                .onRetryExhaustedThrow((spec, sig) -> sig.failure());

        return webClient.get().uri(path)
                .retrieve()
                // Spring 6+/Boot 3 expects Predicate<HttpStatusCode> here:
                .onStatus(HttpStatusCode::is4xxClientError, resp -> resp.createException())
                .onStatus(HttpStatusCode::is5xxServerError, resp -> resp.createException())
                .bodyToFlux(PublicHoliday.class)
                .collectList()
                .transformDeferred(RateLimiterOperator.of(rateLimiterRegistry.rateLimiter("nager")))
                .timeout(Duration.ofMillis(25000))
                .retryWhen(retry)
                .doOnError(ex -> log.error("Nager API call failed: {}", ex.toString()));
    }

    private boolean isRetriable(Throwable t) {
        if (t instanceof WebClientResponseException wcre) {
            int status = wcre.getStatusCode().value();
            return status >= 500 && status < 600; // retry 5xx
        }
        return true; // timeouts, IO, etc.
    }
}

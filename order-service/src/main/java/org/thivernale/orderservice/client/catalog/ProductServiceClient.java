package org.thivernale.orderservice.client.catalog;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductServiceClient {
    private final RestClient restClient;

    @CircuitBreaker(name = "catalog-service")
    @Retry(name = "catalog-service", fallbackMethod = "findByCodeFallback")
    public Optional<ProductDto> findByCode(String code) {
        ProductDto productDto = restClient
            .get()
            .uri("/api/products/{code}", code)
            .retrieve()
            .body(ProductDto.class);
        return Optional.ofNullable(productDto);
    }

    Optional<ProductDto> findByCodeFallback(String code, Throwable throwable) {
        // consult expected behaviour with the business, for example implement caching and read from there
        log.info("catalog-service findByCodeFallback: {}, {}", code, throwable.getMessage());
        return Optional.empty();
    }
}

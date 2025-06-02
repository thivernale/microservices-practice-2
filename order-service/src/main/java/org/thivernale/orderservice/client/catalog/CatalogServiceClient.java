package org.thivernale.orderservice.client.catalog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CatalogServiceClient {
    private final RestClient restClient;

    public Optional<ProductDto> findByCode(String code) {
        try {
            ProductDto productDto = restClient
                .get()
                .uri("/api/products/{code}", code)
                .retrieve()
                .body(ProductDto.class);
            return Optional.ofNullable(productDto);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

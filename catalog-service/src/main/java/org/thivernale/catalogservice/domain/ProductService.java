package org.thivernale.catalogservice.domain;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thivernale.catalogservice.ApplicationProperties;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ApplicationProperties applicationProperties;
    private final ObservationRegistry observationRegistry;

    public PagedResult<ProductDto> getProducts(int pageNo) {
        Page<ProductDto> productDtoPage = productRepository.findAll(
                PageRequest.of(
                    Math.max(pageNo - 1, 0),
                    applicationProperties.pageSize(),
                    Sort.by("name")
                        .ascending()))
            .map(productMapper::toDto);

        return new PagedResult<>(
            productDtoPage.getContent(),
            productDtoPage.getTotalElements(),
            productDtoPage.getNumber() + 1L,
            productDtoPage.getTotalPages(),
            productDtoPage.isFirst(),
            productDtoPage.isLast(),
            productDtoPage.hasNext(),
            productDtoPage.hasPrevious()
        );
    }

    public Optional<ProductDto> findByCode(String code) {
        // metric will be exposed at /actuator/metrics/findByCode and
        // traced if a tracing system is configured with spans named "findByCode"
        return Observation.createNotStarted("findByCode", observationRegistry)
            .observe(() -> findByCodeInner(code));
    }

    private Optional<ProductDto> findByCodeInner(String code) {
        return productRepository.findByCode(code)
            .map(productMapper::toDto);
    }
}

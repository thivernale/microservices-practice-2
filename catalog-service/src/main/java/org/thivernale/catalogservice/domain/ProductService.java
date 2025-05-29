package org.thivernale.catalogservice.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thivernale.catalogservice.ApplicationProperties;

import java.util.Optional;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ApplicationProperties applicationProperties;

    public ProductService(ProductRepository productRepository, ApplicationProperties applicationProperties) {
        this.productRepository = productRepository;
        this.applicationProperties = applicationProperties;
    }

    public PagedResult<ProductDto> getProducts(int pageNo) {
        Page<ProductDto> productDtoPage = productRepository.findAll(
                PageRequest.of(
                    Math.max(pageNo - 1, 0),
                    applicationProperties.pageSize(),
                    Sort.by("name")
                        .ascending()))
            .map(ProductMapper::mapToDto);

        return new PagedResult<>(
            productDtoPage.getContent(),
            productDtoPage.getTotalElements(),
            productDtoPage.getNumber() + 1,
            productDtoPage.getTotalPages(),
            productDtoPage.isFirst(),
            productDtoPage.isLast(),
            productDtoPage.hasNext(),
            productDtoPage.hasPrevious()
        );
    }

    public Optional<ProductDto> findByCode(String code) {
        return productRepository.findByCode(code)
            .map(ProductMapper::mapToDto);
    }
}

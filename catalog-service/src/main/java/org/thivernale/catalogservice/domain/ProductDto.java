package org.thivernale.catalogservice.domain;

import java.math.BigDecimal;

/**
 * DTO for {@link Product}
 */
public record ProductDto(String code, String name, String description, String imageUrl, BigDecimal price) {
}

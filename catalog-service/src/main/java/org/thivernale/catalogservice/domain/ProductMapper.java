package org.thivernale.catalogservice.domain;

class ProductMapper {
    static ProductDto mapToDto(Product product) {
        return new ProductDto(product.getCode(), product.getName(), product.getDescription(),
            product.getImageUrl(), product.getPrice());
    }
}

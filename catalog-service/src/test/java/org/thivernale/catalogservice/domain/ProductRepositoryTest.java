package org.thivernale.catalogservice.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(
    properties = {
        "spring.test.database.replace=none",
        "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db"
    }
)
@Sql("/test-data.sql")
//@Import(TestcontainersConfiguration.class)
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    // for demonstration purposes only
    @Test
    void shouldFindAllProducts() {
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(10);
    }

    @Test
    void shouldFindProductByCode() {
        Product product = productRepository.findByCode("book_001")
            .orElseThrow();
        assertThat(product).isNotNull();
        assertThat(product.getCode()).isEqualTo("book_001");
        assertThat(product.getName()).isEqualTo("The Wedding People");
        assertThat(product.getDescription()).isEqualTo("A contemporary novel exploring the complexities of love and " +
            "commitment.");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("19.99"));
    }

    @Test
    void shouldReturnEmptyWhenFindProductByCodeNotFound() {
        assertThat(productRepository.findByCode("book_not_existing")).isEmpty();
    }
}

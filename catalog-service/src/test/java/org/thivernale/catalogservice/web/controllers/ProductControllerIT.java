package org.thivernale.catalogservice.web.controllers;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.thivernale.catalogservice.AbstractIT;
import org.thivernale.catalogservice.domain.ProductDto;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Sql("/test-data.sql")
class ProductControllerIT extends AbstractIT {
    @Test
    public void shouldReturnProducts() {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/products")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("data", hasSize(10))
            .body("totalElements", is(10))
            .body("pageNumber", is(1))
            .body("totalPages", is(1))
            .body("isFirst", is(true))
            .body("isLast", is(true))
            .body("hasPrevious", is(false))
            .body("hasNext", is(false))
        ;
    }

    @Test
    public void shouldGetProductByCode() {
        ProductDto productDto = given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/products/{code}", "book_001")
            .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat()
            .extract()
            .body()
            .as(ProductDto.class);

        assertThat(productDto.code())
            .isEqualTo("book_001");
        assertThat(productDto.name())
            .isEqualTo("The Wedding People");
        assertThat(productDto.description())
            .isEqualTo("A contemporary novel exploring the complexities of love and " +
                "commitment.");
        assertThat(productDto.price())
            .isEqualTo(new BigDecimal("19.99"));
    }

    @Test
    public void shouldReturnNotFoundWhenProductDoesNotExist() {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/products/{code}", "invalid_code")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("status", is(404))
            .body("title", is("Product Not Found"))
            .body("detail", is("Product with code invalid_code not found"));
    }
}

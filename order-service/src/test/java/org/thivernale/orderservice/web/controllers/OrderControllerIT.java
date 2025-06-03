package org.thivernale.orderservice.web.controllers;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.thivernale.orderservice.AbstractIT;
import org.thivernale.orderservice.client.catalog.ProductDto;
import org.thivernale.orderservice.domain.models.CreateOrderRequest;
import org.thivernale.orderservice.domain.models.OrderSummaryDto;
import org.thivernale.orderservice.testdata.TestDataFactory;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;

@Sql("/test_orders.sql")
class OrderControllerIT extends AbstractIT {
    @Nested
    class CreateOrderTests {
        @Test
        public void shouldCreateOrder() {
            CreateOrderRequest request = TestDataFactory.createValidOrderRequest();

            // mock product response from catalog service for each order item
            request.orderItems()
                .stream()
                .map(item -> new ProductDto(item.code(), item.name(), "", "", item.price()))
                .forEach(AbstractIT::mockProductDtoResponse);

            given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("orderNumber", any(String.class));
        }

        @Test
        public void shouldReturnBadRequestWhenMandatoryDataIsMissing() {
            CreateOrderRequest request = TestDataFactory.createOrderRequestWithNoItems();

            given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class GetOrderByOrderNumberTests {
        @Test
        public void shouldGetOrder() {
            String orderNumber = "5b36246b-e7a1-431f-99db-08daafe9bc5b";
            given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/orders/{orderNumber}", orderNumber)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("orderNumber", is(orderNumber))
                .body("orderItems.size()", is(2))
            ;
        }
    }


    @Nested
    class ListOrdersTests {
        @Test
        public void shouldGetOrders() {
            List<OrderSummaryDto> orderDtos = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/orders")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<>() {
                });

            assertThat(orderDtos)
                .hasSize(2);
        }
    }
}

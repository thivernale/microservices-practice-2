package org.thivernale.orderservice.testdata;

import org.jetbrains.annotations.NotNull;
import org.thivernale.orderservice.domain.models.Address;
import org.thivernale.orderservice.domain.models.CreateOrderRequest;
import org.thivernale.orderservice.domain.models.Customer;
import org.thivernale.orderservice.domain.models.OrderItemDto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.instancio.Instancio.gen;
import static org.instancio.Instancio.of;
import static org.instancio.Select.field;

public class TestDataFactory {
    static final List<String> VALID_COUNTRIES = List.of("UK", "Germany", "France");
    static final Set<OrderItemDto> VALID_ORDER_ITEMS = Set.of(
        new OrderItemDto("book_003", "The God of the Woods", BigDecimal.valueOf(18.75), 1)
    );

    public static @NotNull CreateOrderRequest createValidOrderRequest() {
        return of(CreateOrderRequest.class)
            .generate(field(Customer::email), gen()
                .net()
                .email())
            .generate(field(Address::country), gen()
                .oneOf(VALID_COUNTRIES))
            .set(field(CreateOrderRequest::orderItems), VALID_ORDER_ITEMS)
            .create();
    }

    public static @NotNull CreateOrderRequest createOrderRequestWithInvalidDeliveryAddress() {
        return of(CreateOrderRequest.class)
            .generate(field(Customer::email), gen()
                .net()
                .email())
            .set(field(Address::country), "")
            .set(field(CreateOrderRequest::orderItems), VALID_ORDER_ITEMS)
            .create();
    }

    public static @NotNull CreateOrderRequest createOrderRequestWithInvalidCustomer() {
        return of(CreateOrderRequest.class)
            .generate(field(Customer::email), gen()
                .net()
                .email())
            .set(field(Customer::phone), "")
            .generate(field(Address::country), gen()
                .oneOf(VALID_COUNTRIES))
            .set(field(CreateOrderRequest::orderItems), VALID_ORDER_ITEMS)
            .create();
    }

    public static @NotNull CreateOrderRequest createOrderRequestWithNoItems() {
        return of(CreateOrderRequest.class)
            .generate(field(Customer::email), gen()
                .net()
                .email())
            .generate(field(Address::country), gen()
                .oneOf(VALID_COUNTRIES))
            .set(field(CreateOrderRequest::orderItems), Collections.emptySet())
            .create();
    }
}

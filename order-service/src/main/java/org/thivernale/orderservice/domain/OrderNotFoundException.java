package org.thivernale.orderservice.domain;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public static OrderNotFoundException forNumber(String orderNo) {
        return new OrderNotFoundException("Order with orderNo %s not found".formatted(orderNo));
    }
}

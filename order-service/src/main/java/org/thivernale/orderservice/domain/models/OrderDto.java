package org.thivernale.orderservice.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderDto(
    String orderNumber,
    String username,
    Customer customer,
    Address deliveryAddress,
    OrderStatus status,
    String comments,
    LocalDateTime createdAt,
    Set<OrderItemDto> orderItems) {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public BigDecimal getTotalAmount() {
        return orderItems.stream()
            .map(orderItemDto -> orderItemDto.price()
                .multiply(BigDecimal.valueOf(orderItemDto.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

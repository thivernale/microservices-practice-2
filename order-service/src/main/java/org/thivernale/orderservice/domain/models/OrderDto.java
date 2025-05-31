package org.thivernale.orderservice.domain.models;

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
}

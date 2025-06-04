package org.thivernale.orderservice.domain.models;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderCreatedEvent(
    String eventId,
    String orderNumber,
    Customer customer,
    Address deliveryAddress,
    Set<OrderItemDto> orderItems,
    LocalDateTime createdAt) {
}

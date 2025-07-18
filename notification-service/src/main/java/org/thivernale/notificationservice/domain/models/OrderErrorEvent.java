package org.thivernale.notificationservice.domain.models;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderErrorEvent(
    String eventId,
    String orderNumber,
    Customer customer,
    Address deliveryAddress,
    Set<OrderItemDto> orderItems,
    String reason,
    LocalDateTime createdAt) {
}

package org.thivernale.notificationservice.domain.models;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderDeliveredEvent(
    String eventId,
    String orderNumber,
    Customer customer,
    Address deliveryAddress,
    Set<OrderItemDto> orderItems,
    LocalDateTime createdAt) {
}

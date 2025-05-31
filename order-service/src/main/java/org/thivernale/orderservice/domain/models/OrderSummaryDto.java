package org.thivernale.orderservice.domain.models;

public record OrderSummaryDto(
    String orderNumber,
    OrderStatus status) {
}

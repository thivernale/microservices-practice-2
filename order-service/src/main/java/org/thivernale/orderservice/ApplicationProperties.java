package org.thivernale.orderservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "orders")
public record ApplicationProperties(
    String orderEventsExchange,
    String newOrdersQueue,
    String deliveredOrdersQueue,
    String cancelledOrdersQueue,
    String errorOrdersQueue) {
}

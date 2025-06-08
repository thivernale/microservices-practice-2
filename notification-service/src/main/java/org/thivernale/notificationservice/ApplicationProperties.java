package org.thivernale.notificationservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "notifications")
public record ApplicationProperties(
    String supportEmail,
    String orderEventsExchange,
    String newOrdersQueue,
    String deliveredOrdersQueue,
    String cancelledOrdersQueue,
    String errorOrdersQueue) {
}

package org.thivernale.orderservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.thivernale.orderservice.ApplicationProperties;
import org.thivernale.orderservice.domain.models.OrderCancelledEvent;
import org.thivernale.orderservice.domain.models.OrderCreatedEvent;
import org.thivernale.orderservice.domain.models.OrderDeliveredEvent;
import org.thivernale.orderservice.domain.models.OrderErrorEvent;

@Component
@RequiredArgsConstructor
class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties applicationProperties;

    void publish(Object payload) {
        // Pattern Matching for switch https://openjdk.org/jeps/406
        String routingKey = switch (payload) {
            case OrderCreatedEvent e -> applicationProperties.newOrdersQueue();
            case OrderDeliveredEvent e -> applicationProperties.deliveredOrdersQueue();
            case OrderCancelledEvent e -> applicationProperties.cancelledOrdersQueue();
            case OrderErrorEvent e -> applicationProperties.errorOrdersQueue();
            default -> throw new IllegalStateException("Unexpected value: " + payload.getClass());
        };

        sendMessage(routingKey, payload);
    }

    void sendMessage(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(
            applicationProperties.orderEventsExchange(),
            routingKey,
            payload
        );
    }
}

package org.thivernale.notificationservice.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.thivernale.notificationservice.domain.NotificationService;
import org.thivernale.notificationservice.domain.OrderEvent;
import org.thivernale.notificationservice.domain.OrderEventRepository;
import org.thivernale.notificationservice.domain.models.OrderCancelledEvent;
import org.thivernale.notificationservice.domain.models.OrderCreatedEvent;
import org.thivernale.notificationservice.domain.models.OrderDeliveredEvent;
import org.thivernale.notificationservice.domain.models.OrderErrorEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventHandler {
    private final OrderEventRepository orderEventRepository;
    private final NotificationService notificationService;

    @RabbitListener(queues = {"${notifications.new-orders-queue}"})
    public void receiveOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Order event already exists: {}", event.eventId());
            return;
        }

        notificationService.sendOrderCreatedNotification(event);
        orderEventRepository.save(new OrderEvent(event.eventId()));
    }

    @RabbitListener(queues = {"${notifications.delivered-orders-queue}"})
    public void receiveOrderDeliveredEvent(OrderDeliveredEvent event) {
        log.info("Received OrderDeliveredEvent: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Order event already exists: {}", event.eventId());
            return;
        }

        notificationService.sendOrderDeliveredNotification(event);
        orderEventRepository.save(new OrderEvent(event.eventId()));
    }

    @RabbitListener(queues = {"${notifications.cancelled-orders-queue}"})
    public void receiveOrderCancelledEvent(OrderCancelledEvent event) {
        log.info("Received OrderCancelledEvent: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Order event already exists: {}", event.eventId());
            return;
        }

        notificationService.sendOrderCancelledNotification(event);
        orderEventRepository.save(new OrderEvent(event.eventId()));
    }

    @RabbitListener(queues = {"${notifications.error-orders-queue}"})
    public void receiveOrderErrorEvent(OrderErrorEvent event) {
        log.info("Received OrderErrorEvent: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Order event already exists: {}", event.eventId());
            return;
        }

        notificationService.sendErrorCreateNotification(event);
        orderEventRepository.save(new OrderEvent(event.eventId()));
    }
}

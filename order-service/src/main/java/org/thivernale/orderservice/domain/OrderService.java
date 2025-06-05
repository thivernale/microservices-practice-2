package org.thivernale.orderservice.domain;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thivernale.orderservice.domain.models.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {
    private static final List<String> DELIVERY_COUNTRY_LIST = List.of("UK", "US", "DE");

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;

    public CreateOrderResponse createOrder(String username, CreateOrderRequest request) {

        orderValidator.validate(request);

        Order order = orderMapper.toEntity(request);
        order.setOrderNumber(UUID.randomUUID()
            .toString());
        order.setStatus(OrderStatus.NEW);
        order.setUsername(username);

        Order savedOrder = orderRepository.save(order);
        log.info("Created order with orderNumber: {}", order.getOrderNumber());

        orderEventService.save(orderMapper.toOrderCreatedEvent(savedOrder));

        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }

    public Optional<OrderDto> getUserOrder(String username, String orderNumber) {
        return orderRepository.findByUsernameAndOrderNumber(username, orderNumber)
            .map(orderMapper::toDto);
    }

    public List<OrderSummaryDto> getOrders(String username) {
        return orderRepository.findOrdersByUsername(username);
    }

    public void processOrders() {
        orderRepository.findAllByStatus(OrderStatus.NEW)
            .forEach(order -> {
                processOrder(order);
            });
    }

    private void processOrder(Order order) {
        try {
            if (isDeliveryAvailable(order)) {
                orderRepository.updateStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
                orderEventService.save(orderMapper.toOrderDeliveredEvent(order));
            } else {
                orderRepository.updateStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                orderEventService.save(orderMapper.toOrderCancelledEvent(order, "Delivery to %s not available".formatted(order.getDeliveryAddress()
                    .country())));
            }
        } catch (RuntimeException e) {
            orderRepository.updateStatus(order.getOrderNumber(), OrderStatus.ERROR);
            orderEventService.save(orderMapper.toOrderErrorEvent(order, e.getMessage()));
        }
    }

    private boolean isDeliveryAvailable(Order order) {
        return DELIVERY_COUNTRY_LIST.contains(order.getDeliveryAddress()
            .country());
    }
}

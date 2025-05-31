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
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public CreateOrderResponse createOrder(String username, CreateOrderRequest request) {
        Order order = orderMapper.toEntity(request);
        order.setOrderNumber(UUID.randomUUID()
            .toString());
        order.setStatus(OrderStatus.NEW);
        order.setUsername(username);

        Order savedOrder = orderRepository.save(order);
        log.info("Created order with orderNumber: {}", order.getOrderNumber());

        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }

    public Optional<OrderDto> getUserOrder(String username, String orderNumber) {
        return orderRepository.findByUsernameAndOrderNumber(username, orderNumber)
            .map(orderMapper::toDto);
    }

    public List<OrderSummaryDto> getOrders(String username) {
        return orderRepository.findOrdersByUsername(username);
    }
}

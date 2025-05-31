package org.thivernale.orderservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thivernale.orderservice.domain.models.OrderSummaryDto;

import java.util.List;
import java.util.Optional;

interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUsernameAndOrderNumber(String username, String orderNumber);

    List<OrderSummaryDto> findOrdersByUsername(String username);
}

package org.thivernale.orderservice.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.thivernale.orderservice.domain.models.OrderStatus;
import org.thivernale.orderservice.domain.models.OrderSummaryDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "orderItems")
    /*@Query("""
        select distinct o from Order o left join fetch o.orderItems
                where o.username = :username and o.orderNumber = :orderNumber
        """)*/
    Optional<Order> findByUsernameAndOrderNumber(String username, String orderNumber);

    Optional<Order> findByOrderNumber(String orderNumber);

    List<OrderSummaryDto> findOrdersByUsername(String username);

    List<Order> findAllByStatus(OrderStatus status);

    default void updateStatus(String orderNumber, OrderStatus orderStatus) {
        Order order = findByOrderNumber(orderNumber).orElseThrow();
        order.setStatus(orderStatus);
        order.setUpdatedAt(LocalDateTime.now());
        save(order);
    }
}

package org.thivernale.orderservice.domain;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.thivernale.orderservice.domain.models.OrderEventType;

import java.util.List;

interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM OrderEvent o WHERE o.type = :type")
    List<OrderEvent> findOrderEventsForProcessing(OrderEventType type, Pageable pageable);
}

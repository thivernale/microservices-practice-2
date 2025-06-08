package org.thivernale.notificationservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {
    boolean existsByEventId(String eventId);
}

package org.thivernale.orderservice.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thivernale.orderservice.domain.OrderService;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
class OrderProcessingJob {
    private final OrderService orderService;

    @Scheduled(cron = "${app.schedule.process-orders:-}")
    @SchedulerLock(name = "processOrders")
    public void publishEvents() {
        LockAssert.assertLocked();
        log.info("Processing orders at {}", Instant.now());
        orderService.processOrders();
    }
}

package org.thivernale.orderservice.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thivernale.orderservice.domain.OrderEventService;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
class OrderEventPublisherJob {
    private final OrderEventService orderEventService;

    @Scheduled(cron = "${app.schedule.publish-events:-}")
    @SchedulerLock(name = "publishOrderEvents")
    public void publishEvents() {
        LockAssert.assertLocked();
        log.info("Publishing new order events at {}", Instant.now());
        orderEventService.publishEvents();
    }
}

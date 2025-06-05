package org.thivernale.orderservice.jobs;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thivernale.orderservice.domain.OrderEventService;

@Component
@RequiredArgsConstructor
class OrderEventPublisherJob {
    private final OrderEventService orderEventService;

    @Scheduled(cron = "${app.schedule.publish-events:-}")
    @SchedulerLock(name = "publishOrderEvents")
    public void publishEvents() {
        LockAssert.assertLocked();
        orderEventService.publishEvents();
    }
}

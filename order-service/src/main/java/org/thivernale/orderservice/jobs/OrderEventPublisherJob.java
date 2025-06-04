package org.thivernale.orderservice.jobs;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thivernale.orderservice.domain.OrderEventService;

@Component
@RequiredArgsConstructor
class OrderEventPublisherJob {
    private final OrderEventService orderEventService;

    @Scheduled(cron = "${app.schedule.publish-events:-}")
    public void publishEvents() {
        orderEventService.publishEvents();
    }
}

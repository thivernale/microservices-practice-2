package org.thivernale.orderservice.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thivernale.orderservice.domain.models.OrderCancelledEvent;
import org.thivernale.orderservice.domain.models.OrderCreatedEvent;
import org.thivernale.orderservice.domain.models.OrderDeliveredEvent;
import org.thivernale.orderservice.domain.models.OrderErrorEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventService {
    private final OrderEventRepository orderEventRepository;
    private final OrderEventMapper orderEventMapper;
    private final ObjectMapper objectMapper;
    private final OrderEventPublisher orderEventPublisher;

    void save(Object payload) {
        OrderEvent orderEvent = switch (payload) {
            case OrderCreatedEvent e -> orderEventMapper.toEntity(e, this);
            case OrderDeliveredEvent e -> orderEventMapper.toEntity(e, this);
            case OrderCancelledEvent e -> orderEventMapper.toEntity(e, this);
            case OrderErrorEvent e -> orderEventMapper.toEntity(e, this);
            default -> throw new IllegalStateException("Unexpected value: " + payload.getClass());
        };

        orderEventRepository.save(orderEvent);
    }

    @Named("toJsonPayload")
    <T> String toJsonPayload(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T fromJsonPayload(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void publishEvents() {
        orderEventRepository.findAll(Sort.by(Sort.Direction.ASC, "createdAt"))
            .forEach(orderEvent -> {
                Class<?> clazz = switch (orderEvent.getType()) {
                    case CREATED -> OrderCreatedEvent.class;
                    case DELIVERED -> OrderDeliveredEvent.class;
                    case CANCELLED -> OrderCancelledEvent.class;
                    case ERROR -> OrderErrorEvent.class;
                };
                orderEventPublisher.publish(fromJsonPayload(orderEvent.getPayload(), clazz));
                orderEventRepository.deleteById(orderEvent.getId());
            });
    }
}

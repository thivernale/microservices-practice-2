package org.thivernale.orderservice.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thivernale.orderservice.domain.models.OrderCreatedEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventService {
    private final OrderEventRepository orderEventRepository;
    private final OrderEventMapper orderEventMapper;
    private final ObjectMapper objectMapper;
    private final OrderEventPublisher orderEventPublisher;

    void save(OrderCreatedEvent dto) {
        orderEventRepository.save(orderEventMapper.toEntity(dto, this));
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
        log.info("Publishing order events");
        orderEventRepository.findAll(Sort.by(Sort.Direction.ASC, "createdAt"))
            .forEach(orderEvent -> {
                Class<?> clazz = switch (orderEvent.getType()) {
                    case CREATED -> OrderCreatedEvent.class;
                    case DELIVERED -> null;
                    case CANCELLED -> null;
                    case ERROR -> null;
                };
                orderEventPublisher.publish(fromJsonPayload(orderEvent.getPayload(), clazz));

                orderEventRepository.deleteById(orderEvent.getId());
            });
    }
}

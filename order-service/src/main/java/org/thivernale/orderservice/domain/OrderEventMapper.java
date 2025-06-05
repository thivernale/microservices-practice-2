package org.thivernale.orderservice.domain;

import org.mapstruct.*;
import org.thivernale.orderservice.domain.models.OrderCancelledEvent;
import org.thivernale.orderservice.domain.models.OrderCreatedEvent;
import org.thivernale.orderservice.domain.models.OrderDeliveredEvent;
import org.thivernale.orderservice.domain.models.OrderErrorEvent;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
interface OrderEventMapper {

    @Mapping(target = "type", constant = "CREATED")
    @Mapping(target = "payload", expression = "java(orderEventService.toJsonPayload(dto))")
    OrderEvent toEntity(OrderCreatedEvent dto, @Context OrderEventService orderEventService);

    @Mapping(target = "type", constant = "DELIVERED")
    @Mapping(target = "payload", expression = "java(orderEventService.toJsonPayload(dto))")
    OrderEvent toEntity(OrderDeliveredEvent dto, @Context OrderEventService orderEventService);

    @Mapping(target = "type", constant = "CANCELLED")
    @Mapping(target = "payload", expression = "java(orderEventService.toJsonPayload(dto))")
    OrderEvent toEntity(OrderCancelledEvent dto, @Context OrderEventService orderEventService);

    @Mapping(target = "type", constant = "ERROR")
    @Mapping(target = "payload", expression = "java(orderEventService.toJsonPayload(dto))")
    OrderEvent toEntity(OrderErrorEvent dto, @Context OrderEventService orderEventService);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderEvent partialUpdate(OrderCreatedEvent dto,
                             @MappingTarget
                             OrderEvent orderEvent);
}

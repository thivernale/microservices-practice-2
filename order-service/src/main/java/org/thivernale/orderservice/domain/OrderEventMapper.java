package org.thivernale.orderservice.domain;

import org.mapstruct.*;
import org.thivernale.orderservice.domain.models.OrderCreatedEvent;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
interface OrderEventMapper {

    @Mapping(target = "type", constant = "CREATED")
    @Mapping(target = "payload", expression = "java(orderEventService.toJsonPayload(dto))")
    OrderEvent toEntity(OrderCreatedEvent dto, @Context OrderEventService orderEventService);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderEvent partialUpdate(OrderCreatedEvent dto,
                             @MappingTarget
                             OrderEvent orderEvent);
}

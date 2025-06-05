package org.thivernale.orderservice.domain;

import org.mapstruct.*;
import org.thivernale.orderservice.domain.models.*;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface OrderMapper {
/*
    OrderItem toItemEntity(OrderItemDto orderItemDto);

    OrderItemDto toItemDto(OrderItem orderItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderItem partialUpdateItem(OrderItemDto orderItemDto,
                                @MappingTarget
                                OrderItem orderItem);
*/

    Order toEntity(CreateOrderRequest orderRequest);

    @AfterMapping
    default void linkOrderItems(@MappingTarget Order order) {
        order.getOrderItems()
            .forEach(orderItem -> orderItem.setOrder(order));
    }

    OrderDto toDto(Order order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(OrderDto orderDto,
                        @MappingTarget
                        Order order);

    @Mapping(target = "eventId", expression = "java(generateEventId())")
    OrderCreatedEvent toOrderCreatedEvent(Order order);

    @Mapping(target = "eventId", expression = "java(generateEventId())")
    @Mapping(target = "createdAt", source = "updatedAt")
    OrderDeliveredEvent toOrderDeliveredEvent(Order order);

    @Mapping(target = "eventId", expression = "java(generateEventId())")
    @Mapping(target = "createdAt", source = "updatedAt")
    @Mapping(target = "reason", expression = "java(reason)")
    OrderCancelledEvent toOrderCancelledEvent(Order order, @Context String reason);

    @Mapping(target = "eventId", expression = "java(generateEventId())")
    @Mapping(target = "createdAt", source = "updatedAt")
    @Mapping(target = "reason", expression = "java(reason)")
    OrderErrorEvent toOrderErrorEvent(Order order, @Context String reason);

    default String generateEventId() {
        return UUID.randomUUID()
            .toString();
    }
}

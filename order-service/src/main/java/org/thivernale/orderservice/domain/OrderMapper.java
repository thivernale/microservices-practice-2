package org.thivernale.orderservice.domain;

import org.mapstruct.*;
import org.thivernale.orderservice.domain.models.CreateOrderRequest;
import org.thivernale.orderservice.domain.models.OrderDto;

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
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));
    }

    OrderDto toDto(Order order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(OrderDto orderDto,
                        @MappingTarget
                        Order order);
}

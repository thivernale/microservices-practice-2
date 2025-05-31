package org.thivernale.orderservice.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.thivernale.orderservice.domain.OrderNotFoundException;
import org.thivernale.orderservice.domain.OrderService;
import org.thivernale.orderservice.domain.SecurityService;
import org.thivernale.orderservice.domain.models.CreateOrderRequest;
import org.thivernale.orderservice.domain.models.CreateOrderResponse;
import org.thivernale.orderservice.domain.models.OrderDto;
import org.thivernale.orderservice.domain.models.OrderSummaryDto;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Slf4j
@RequiredArgsConstructor
class OrderController {
    private final OrderService orderService;
    private final SecurityService securityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody final CreateOrderRequest request) {
        String username = securityService.getCurrentUsername();
        log.info("Creating order for user {}", username);
        return orderService.createOrder(username, request);
    }

    @GetMapping
    List<OrderSummaryDto> getOrders() {
        String username = securityService.getCurrentUsername();
        log.info("Getting orders for user {}", username);
        return orderService.getOrders(username);
    }

    @GetMapping("/{orderNumber}")
    OrderDto getOrder(@PathVariable(value = "orderNumber") final String orderNumber) {
        String username = securityService.getCurrentUsername();
        log.info("Getting order by orderNumber {}", orderNumber);
        return orderService.getUserOrder(username, orderNumber)
            .orElseThrow(() -> OrderNotFoundException.forNumber(orderNumber));
    }
}

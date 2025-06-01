package org.thivernale.orderservice.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.thivernale.orderservice.domain.OrderService;
import org.thivernale.orderservice.domain.SecurityService;
import org.thivernale.orderservice.domain.models.CreateOrderRequest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.thivernale.orderservice.testdata.TestDataFactory.*;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {
    @MockitoBean
    private OrderService orderService;
    @MockitoBean
    private SecurityService securityService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    public static Stream<Arguments> createOrderRequestProvider() {
        return Stream.of(
            arguments(named("Order with invalid customer", createOrderRequestWithInvalidCustomer())),
            arguments(named("Order with invalid delivery address", createOrderRequestWithInvalidDeliveryAddress())),
            arguments(named("Order with empty items array", createOrderRequestWithNoItems()))
        );
    }

    @BeforeEach
    void setUp() {
        given(securityService.getCurrentUsername())
            .willReturn("user");
    }

    @ParameterizedTest(name = "[{index}]-{0}")
    @MethodSource("createOrderRequestProvider")
    public void shouldReturnBadRequestWhenOrderPayloadIsInvalid(CreateOrderRequest request) throws Exception {
        given(orderService.createOrder(eq("user"), any(CreateOrderRequest.class))).willReturn(null);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status()
                .isBadRequest());
    }
}

package org.thivernale.orderservice.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.thivernale.orderservice.AbstractIT;
import org.thivernale.orderservice.WithMockJwtAuth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetOrdersTests extends AbstractIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockJwtAuth
    public void getOrdersTests() throws Exception {
        mockMvc.perform(get("/api/orders"))
            .andExpect(status().isOk());
    }
}

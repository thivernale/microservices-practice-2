package org.thivernale.orderservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.thivernale.orderservice.client.catalog.ProductDto;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractIT {
    @LocalServerPort
    private int port;

    protected static void mockProductDtoResponse(ProductDto productDto) {
        try {
            stubFor(get("/api/products/" + productDto.code())
                .willReturn(okJson(new ObjectMapper().writeValueAsString(productDto))));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }
}

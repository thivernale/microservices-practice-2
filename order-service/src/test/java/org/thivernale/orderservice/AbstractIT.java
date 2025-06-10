package org.thivernale.orderservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.keycloak.OAuth2Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.thivernale.orderservice.client.catalog.ProductDto;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AbstractIT {
    // use bookstore-frontend client because it has password flow enabled
    private static final String CLIENT_ID = "bookstore-frontend";
    private static final String CLIENT_SECRET = "";
    private static final String USERNAME = "user@example.com";
    private static final String PASSWORD = "password";

    @LocalServerPort
    private int port;

    @Autowired
    private OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

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

    protected String getToken() {
        RestTemplate restTemplate = new RestTemplate();

        // "http://localhost:8180/realms/bookstore"
        String issuerUri = oAuth2ResourceServerProperties.getJwt()
            .getIssuerUri();

        // client_id=bookstore-frontend&grant_type=password&username=<username>&password=<password>&scopes=openid roles
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(OAuth2Constants.GRANT_TYPE, OAuth2Constants.PASSWORD);
        body.add(OAuth2Constants.CLIENT_ID, CLIENT_ID);
        //body.add(OAuth2Constants.CLIENT_SECRET, CLIENT_SECRET);
        body.add(OAuth2Constants.USERNAME, USERNAME);
        body.add(OAuth2Constants.PASSWORD, PASSWORD);

        RequestEntity<?> request = RequestEntity.post(URI.create(issuerUri))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body);
        KeyCloakToken keyCloakToken = restTemplate.postForObject(issuerUri + "/protocol/openid-connect/token", request, KeyCloakToken.class);
        assert keyCloakToken != null;
        return keyCloakToken.accessToken();
    }
}

record KeyCloakToken(@JsonProperty("access_token") String accessToken) {
}

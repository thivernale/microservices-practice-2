package org.thivernale.orderservice;

import com.github.tomakehurst.wiremock.client.WireMock;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    private static final String KEYCLOAK_IMAGE_NAME = "quay.io/keycloak/keycloak:26.2";
    private static final String REALM_IMPORT_FILE = "/test-realm-export.json";
    private static final String REALM_NAME = "bookstore";

    static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:3.6.0");

    @Bean
    DynamicPropertyRegistrar apiPropertiesRegistrar(WireMockContainer wiremockServer, KeycloakContainer keycloakContainer) {
        return registry -> {
            registry.add("orders.catalog-service-url", wiremockServer::getBaseUrl);
            registry.add(
                "spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "/realms/" + REALM_NAME
            );
        };
    }

    @Bean
    WireMockContainer wireMockServer() {
        wiremockServer.start();
        WireMock.configureFor(wiremockServer.getHost(), wiremockServer.getPort());
        return wiremockServer;
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));
    }

    @Bean
    @ServiceConnection
    RabbitMQContainer rabbitContainer() {
        return new RabbitMQContainer(DockerImageName.parse("rabbitmq:4.1.0-alpine"));
    }

    @Bean
    KeycloakContainer keycloakContainer() {
        return new KeycloakContainer(KEYCLOAK_IMAGE_NAME)
            .withRealmImportFile(REALM_IMPORT_FILE);
    }
}

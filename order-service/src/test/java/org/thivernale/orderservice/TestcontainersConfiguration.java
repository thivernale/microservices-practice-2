package org.thivernale.orderservice;

import com.github.tomakehurst.wiremock.client.WireMock;
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

    static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:3.6.0");

    @Bean
    DynamicPropertyRegistrar apiPropertiesRegistrar(WireMockContainer wiremockServer) {
        return registry -> registry.add("orders.catalog-service-url", wiremockServer::getBaseUrl);
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
}

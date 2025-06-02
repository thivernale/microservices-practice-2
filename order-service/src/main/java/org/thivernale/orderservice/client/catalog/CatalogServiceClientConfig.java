package org.thivernale.orderservice.client.catalog;

import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.thivernale.orderservice.ApplicationProperties;

import java.time.Duration;

@Configuration
public class CatalogServiceClientConfig {
    @Bean
    RestClient restClient(RestClient.Builder builder, ApplicationProperties applicationProperties) {
        return builder
            .baseUrl(applicationProperties.catalogServiceUrl())
            .requestFactory(clientHttpRequestFactory())
            .build();
    }

    @Bean
    ClientHttpRequestFactory clientHttpRequestFactory() {
        return ClientHttpRequestFactoryBuilder.detect()
            .build(ClientHttpRequestFactorySettings.defaults()
                .withConnectTimeout(Duration.ofSeconds(5))
                .withReadTimeout(Duration.ofSeconds(5)));
    }
}

package org.thivernale.orderservice.client.catalog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.thivernale.orderservice.ApplicationProperties;

@Configuration
public class CatalogServiceClientConfig {
    @Bean
    RestClient restClient(RestClient.Builder builder, ApplicationProperties applicationProperties) {
        return builder
            .baseUrl(applicationProperties.catalogServiceUrl())
            .build();
    }
}

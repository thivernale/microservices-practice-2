package org.thivernale.apigateway;

import jakarta.annotation.PostConstruct;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.utils.Constants;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class RoutesConfig {
    private final RouteDefinitionLocator routeDefinitionLocator;
    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    public RoutesConfig(RouteDefinitionLocator routeDefinitionLocator, SwaggerUiConfigProperties swaggerUiConfigProperties) {
        this.routeDefinitionLocator = routeDefinitionLocator;
        this.swaggerUiConfigProperties = swaggerUiConfigProperties;
    }

    @PostConstruct
    public void init() {
        List<RouteDefinition> definitionList = routeDefinitionLocator.getRouteDefinitions()
            .collectList()
            .block();
        if (definitionList == null) {
            return;
        }

        swaggerUiConfigProperties.setUrls(
            definitionList.stream()
                .filter(routeDefinition -> routeDefinition.getId()
                    .endsWith("-service"))
                .map(routeDefinition -> routeDefinition.getId()
                    .replace("-service", ""))
                .map(service -> new AbstractSwaggerUiConfigProperties.SwaggerUrl(service, Constants.DEFAULT_API_DOCS_URL + "/" + service, null))
                .collect(Collectors.toSet()));

    }
}

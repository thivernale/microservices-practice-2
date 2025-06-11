package org.thivernale.orderservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Value("${app.urls.api-gateway}/order")
    private String apiGatewayUrl;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Order Service API")
                .version("1.0.0"))
            .addSecurityItem(new SecurityRequirement().addList("Authorization"))
            .addServersItem(new Server().url(apiGatewayUrl))
            .components(new Components().addSecuritySchemes(
                "security_auth",
                new SecurityScheme()
                    .in(SecurityScheme.In.HEADER)
                    .type(SecurityScheme.Type.OAUTH2)
                    .flows(new OAuthFlows().authorizationCode(new OAuthFlow()
                        .authorizationUrl(issuerUri + "/protocol/openid-connect/auth")
                        .tokenUrl(issuerUri + "/protocol/openid-connect/token")
                        .scopes(new Scopes().addString("openid", "openid scope"))))
            ));
    }
}

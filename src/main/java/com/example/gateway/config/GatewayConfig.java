package com.example.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Rota para documentação da API
                .route("api-docs", r -> r.path("/api-docs/**")
                        .uri("http://localhost:8080/swagger-ui.html"))
                
                // Rota para health check
                .route("health", r -> r.path("/health/**")
                        .uri("http://localhost:8080/actuator/health"))
                
                // Rota para métricas
                .route("metrics", r -> r.path("/metrics/**")
                        .uri("http://localhost:8080/actuator/metrics"))
                
                .build();
    }
}

package com.example.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            String requestId = request.getId();
            String method = request.getMethod().toString();
            String path = request.getURI().getPath();
            String remoteAddress = "unknown";
            if (request.getRemoteAddress() != null && request.getRemoteAddress().getAddress() != null) {
                remoteAddress = request.getRemoteAddress().getAddress().getHostAddress();
            }
            String userAgent = request.getHeaders().getFirst("User-Agent");
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            logger.info("=== REQUEST START ===");
            logger.info("Request ID: {}", requestId);
            logger.info("Timestamp: {}", timestamp);
            logger.info("Method: {}", method);
            logger.info("Path: {}", path);
            logger.info("Remote Address: {}", remoteAddress);
            logger.info("User-Agent: {}", userAgent);
            logger.info("Headers: {}", request.getHeaders().toSingleValueMap());

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                ServerHttpResponse response = exchange.getResponse();
                String responseTimestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                
                logger.info("=== RESPONSE END ===");
                logger.info("Request ID: {}", requestId);
                logger.info("Response Timestamp: {}", responseTimestamp);
                logger.info("Status: {}", response.getStatusCode());
                logger.info("Response Headers: {}", response.getHeaders().toSingleValueMap());
                logger.info("==================");
            }));
        };
    }

    public static class Config {
        // Configurações do filtro podem ser adicionadas aqui
    }
}

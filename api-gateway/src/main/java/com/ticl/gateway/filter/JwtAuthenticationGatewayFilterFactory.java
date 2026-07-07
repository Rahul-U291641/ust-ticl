package com.ticl.gateway.filter;

import com.ticl.gateway.config.SecurityProperties;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Log4j2
public class JwtAuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilterFactory.Config> {

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Autowired
    private SecurityProperties securityProperties;

    private final WebClient webClient = WebClient.create();

    public JwtAuthenticationGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();

            if (isPublicUrl(path)) {
                log.debug("Public URL accessed: {}", path);
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.error("Missing or invalid Authorization header for path: {}", path);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return webClient.get()
                    .uri(authServiceUrl + "/auth/validate")
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .retrieve()
                    .bodyToMono(ValidateTokenResponse.class)
                    .flatMap(validateResponse -> {
                        log.info("Token validated for user: {}, role: {}", validateResponse.getUsername(), validateResponse.getRole());
                        var mutatedRequest = exchange.getRequest().mutate()
                                .header("X-Auth-Username", validateResponse.getUsername())
                                .header("X-Auth-Role", validateResponse.getRole())
                                .build();
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    })
                    .onErrorResume(e -> {
                        log.error("Token validation failed: {}", e.getMessage());
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        };
    }

    private boolean isPublicUrl(String path) {
        return securityProperties.getPublicUrls().stream()
                .anyMatch(publicUrl -> path.matches(publicUrl.replace("**", ".*")));
    }

    @Data
    static class ValidateTokenResponse {
        private String username;
        private String role;
    }

    public static class Config {}
}

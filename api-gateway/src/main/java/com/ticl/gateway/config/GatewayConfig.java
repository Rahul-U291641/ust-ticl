package com.ticl.gateway.config;

import com.ticl.gateway.filter.JwtAuthenticationGatewayFilterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationGatewayFilterFactory jwtAuthenticationGatewayFilterFactory;

    @Autowired
    private RouteProperties routeProperties;

    @Autowired
    private CorsProperties corsProperties;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri(routeProperties.getAuthServiceUrl()))
                .route("order-service", r -> r
                        .path("/orders/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(jwtAuthenticationGatewayFilterFactory.apply(new JwtAuthenticationGatewayFilterFactory.Config())))
                        .uri(routeProperties.getOrderServiceUrl()))
                .route("inventory-service", r -> r
                        .path("/inventory/**")
                        .filters(f -> f
                                .filter(jwtAuthenticationGatewayFilterFactory.apply(new JwtAuthenticationGatewayFilterFactory.Config())))
                        .uri(routeProperties.getInventoryServiceUrl()))
                .route("alert-service", r -> r
                        .path("/alerts/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(jwtAuthenticationGatewayFilterFactory.apply(new JwtAuthenticationGatewayFilterFactory.Config())))
                        .uri(routeProperties.getAlertServiceUrl()))
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(corsProperties.getAllowedOrigins());
        corsConfig.setMaxAge(corsProperties.getMaxAge());
        corsConfig.setAllowedMethods(corsProperties.getAllowedMethods());
        corsConfig.setAllowedHeaders(corsProperties.getAllowedHeaders());
        corsConfig.setAllowCredentials(corsProperties.isAllowCredentials());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}

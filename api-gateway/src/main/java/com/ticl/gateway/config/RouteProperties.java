package com.ticl.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "routes")
public class RouteProperties {
    private String authServiceUrl;
    private String orderServiceUrl;
    private String inventoryServiceUrl;
    private String alertServiceUrl;
}

package com.ticl.gateway;

import com.ticl.gateway.config.CorsProperties;
import com.ticl.gateway.config.RouteProperties;
import com.ticl.gateway.config.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({RouteProperties.class, CorsProperties.class, SecurityProperties.class})
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}

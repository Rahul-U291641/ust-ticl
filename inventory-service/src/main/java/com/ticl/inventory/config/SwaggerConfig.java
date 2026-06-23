package com.ticl.inventory.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI Configuration
 * 
 * This configuration bean defines:
 * 1. API metadata (title, version, description, contact)
 * 2. Security scheme (JWT Bearer authentication)
 * 3. License and other documentation details
 * 
 * Without this bean, Swagger UI would show:
 * - No API title or version
 * - No "Authorize" button for JWT
 * - No security scheme information
 * - Generic/incomplete documentation
 */
@Configuration
public class SwaggerConfig {

    /**
     * Creates and configures the OpenAPI bean that Swagger uses to build documentation
     * 
     * What this does:
     * - Sets API title, version, description (shown at top of Swagger UI)
     * - Defines contact information for API support
     * - Adds license information
     * - Configures JWT Bearer token authentication
     * - Makes "Authorize" button available in Swagger UI
     * 
     * @return OpenAPI configured bean
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // ===== API METADATA =====
                // This info block shows at the top of Swagger UI
                .info(new Info()
                        .title("Inventory Service API")                    // API name shown in header
                        .version("1.0.0")                                 // API version
                        .description("REST API for managing inventory items with role-based access control") // Main description
                        .contact(new Contact()                            // Team contact info
                                .name("Inventory Team")
                                .email("<email>")
                                .url("https://your-domain.com"))
                        .license(new License()                            // License info
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                
                // ===== SECURITY REQUIREMENT =====
                // This makes JWT Bearer authentication a requirement for all endpoints
                // without this, Swagger won't know your API needs auth
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                
                // ===== SECURITY SCHEME DEFINITION =====
                // This defines HOW to authenticate (JWT Bearer tokens)
                // Swagger uses this to show the "Authorize" button and input field
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)           // Type: HTTP-based auth
                                        .scheme("bearer")                         // Scheme: Bearer (HTTP Bearer)
                                        .bearerFormat("JWT")                      // Format: JWT tokens
                                        .description("JWT token required for API access.\n\n" +
                                                "Format: Bearer <your_jwt_token>\n\n" +
                                                "Example: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                        ));
    }
}

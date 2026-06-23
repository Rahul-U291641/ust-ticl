package com.ticl.inventory.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI Endpoint Grouping Configuration
 * 
 * This configuration creates logical groups of related endpoints in Swagger UI.
 * 
 * Without this:
 * - All 5 inventory endpoints appear as a flat list
 * - Hard to organize when you have many microservices
 * 
 * With this:
 * - All inventory endpoints grouped under "Inventory Management"
 * - Clean, organized Swagger UI
 * - Easy to expand/collapse groups
 */
@Configuration
public class OpenApiConfig {

    /**
     * Groups all inventory endpoints under "Inventory Management" tag
     * 
     * What this does:
     * - Creates a collapsible group in Swagger UI
     * - Includes all paths matching /api/v1/inventory/**
     * - Shows up as "Inventory Management" in the UI
     * 
     * Example in UI:
     * ─ Inventory Management (this group)
     *   ├── POST Create Inventory Item
     *   ├── GET Get All Inventory Items
     *   ├── GET Get Item by ID
     *   ├── PUT Update Inventory Item
     *   └── DELETE Delete Inventory Item
     * 
     * @return GroupedOpenApi bean for inventory endpoints
     */
    @Bean
    public GroupedOpenApi inventoryApi() {
        return GroupedOpenApi.builder()
                .group("Inventory Management")              // Group name shown in UI
                .pathsToMatch("/api/v1/inventory/**")       // All paths to include in this group
                .build();
    }
}

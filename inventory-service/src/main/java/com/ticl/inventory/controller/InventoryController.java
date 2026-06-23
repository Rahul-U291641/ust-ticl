package com.ticl.inventory.controller;

import com.ticl.inventory.dto.InventoryRequest;
import com.ticl.inventory.dto.InventoryResponse;
import com.ticl.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Inventory Controller
 * 
 * Handles all inventory management operations.
 * All endpoints require JWT Bearer token authentication.
 * 
 * Role requirements:
 * - INVENTORY_MANAGER: Can create, update, delete items
 * - ADMIN: Can perform all operations
 */
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Management", description = "APIs for managing inventory items")
public class InventoryController {

    private final InventoryService service;

    /**
     * Creates a new inventory item
     * 
     * Swagger annotations explain:
     * - @Operation: What this endpoint does
     * - @ApiResponse: All possible response codes and meanings
     * - @RequestBody: Input data required (auto-documented from InventoryRequest)
     */
    @PostMapping
    @PreAuthorize("hasRole('INVENTORY_MANAGER')")
    @Operation(
            summary = "Create a new inventory item",
            description = "Creates a new inventory item with the provided details. Requires INVENTORY_MANAGER role."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Inventory item created successfully",
                    content = @Content(schema = @Schema(implementation = InventoryResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data - missing or invalid fields"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User lacks INVENTORY_MANAGER role"
            )
    })
    public ResponseEntity<?> create(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    /**
     * Retrieves all inventory items with pagination support
     * 
     * Pagination parameters:
     * - page: 0-indexed page number
     * - size: number of items per page
     * - sort: field to sort by (e.g., itemName, itemId)
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    @Operation(
            summary = "Get all inventory items",
            description = "Retrieves a paginated list of all inventory items. Requires ADMIN or INVENTORY_MANAGER role."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of inventory items retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Page.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User lacks required roles"
            )
    })
    public ResponseEntity<Page<InventoryResponse>> getAll(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "itemName")
            @Parameter(description = "Pagination parameters: page (0-indexed), size (items per page), sort (field name)")
            Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    /**
     * Retrieves a specific inventory item by its ID
     * 
     * @param id The UUID of the inventory item
     * @return InventoryResponse with all item details
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    @Operation(
            summary = "Get inventory item by ID",
            description = "Retrieves a specific inventory item using its unique UUID. Requires ADMIN or INVENTORY_MANAGER role."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventory item retrieved successfully",
                    content = @Content(schema = @Schema(implementation = InventoryResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventory item not found for the given ID"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User lacks required roles"
            )
    })
    public InventoryResponse getById(
            @PathVariable
            @Parameter(description = "Unique identifier (UUID) of the inventory item", example = "550e8400-e29b-41d4-a716-446655440000")
            UUID id) {
        return service.getById(id);
    }

    /**
     * Updates an existing inventory item
     * 
     * @param id The UUID of the item to update
     * @param request Updated item details
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('INVENTORY_MANAGER')")
    @Operation(
            summary = "Update an inventory item",
            description = "Updates all fields of an existing inventory item. Requires INVENTORY_MANAGER role."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventory item updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventory item not found for the given ID"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User lacks INVENTORY_MANAGER role"
            )
    })
    public ResponseEntity<?> update(
            @PathVariable
            @Parameter(description = "Unique identifier (UUID) of the inventory item", example = "550e8400-e29b-41d4-a716-446655440000")
            UUID id,
            @Valid @RequestBody
            InventoryRequest request) {

        service.update(id, request);

        return ResponseEntity.ok("Inventory item updated successfully!");
    }

    /**
     * Deletes an inventory item
     * 
     * @param id The UUID of the item to delete
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    @Operation(
            summary = "Delete an inventory item",
            description = "Deletes an inventory item permanently. Requires ADMIN or INVENTORY_MANAGER role."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventory item deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventory item not found for the given ID"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User lacks required roles"
            )
    })
    public ResponseEntity<?> delete(
            @PathVariable
            @Parameter(description = "Unique identifier (UUID) of the inventory item", example = "550e8400-e29b-41d4-a716-446655440000")
            UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Inventory item deleted successfully!");
    }
}

package com.ticl.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
@Schema(description = "Response payload containing inventory item details")
public class InventoryResponse {
    @Schema(description = "Unique identifier for the inventory item", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID itemId;
    
    @Schema(description = "Name of the inventory item", example = "Laptop")
    private String itemName;
    
    @Schema(description = "Supplier name for the item", example = "Dell Inc.")
    private String supplier;
    
    @Schema(description = "Quantity of items in stock", example = "50")
    private Integer quantity;
    
    @Schema(description = "Unit price of the item", example = "999.99")
    private BigDecimal unitPrice;
    
    @Schema(description = "Minimum stock level to trigger reorder", example = "10")
    private Integer reorderThreshold;
}

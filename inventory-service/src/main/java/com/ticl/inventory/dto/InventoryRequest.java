package com.ticl.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Request payload for creating or updating inventory items")
public class InventoryRequest {

    @NotBlank
    @Size(min = 3)
    @Schema(description = "Name of the inventory item", example = "Laptop")
    private String itemName;

    @NotBlank
    @Schema(description = "Supplier name for the item", example = "Dell Inc.")
    private String supplier;

    @Min(0)
    @Schema(description = "Quantity of items in stock", example = "50")
    private Integer quantity;

    @Positive
    @Schema(description = "Unit price of the item", example = "999.99")
    private BigDecimal unitPrice;

    @Positive
    @Schema(description = "Minimum stock level to trigger reorder", example = "10")
    private Integer reorderThreshold;
}

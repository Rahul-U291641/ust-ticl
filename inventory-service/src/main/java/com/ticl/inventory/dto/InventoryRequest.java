package com.ticl.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InventoryRequest {

    @NotBlank
    @Size(min = 3)
    private String itemName;

    @NotBlank
    private String supplier;

    @Min(0)
    private Integer quantity;

    @Positive
    private BigDecimal unitPrice;

    @Positive
    private Integer reorderThreshold;
}

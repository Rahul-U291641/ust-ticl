package com.ticl.inventory.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
public class InventoryResponse {
    private UUID itemId;
    private String itemName;
    private String supplier;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Integer reorderThreshold;
}

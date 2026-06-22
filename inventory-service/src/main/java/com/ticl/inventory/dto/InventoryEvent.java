package com.ticl.inventory.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class InventoryEvent {
    private UUID itemId;
    private String itemName;
    private Integer quantity;
    private Integer reorderThreshold;
    private String eventType;
    private LocalDateTime eventTime;
}

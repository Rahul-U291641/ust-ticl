package com.ticl.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    private UUID orderItemId;
    private UUID itemId;
    private int quantity;
    private double unitPrice;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}

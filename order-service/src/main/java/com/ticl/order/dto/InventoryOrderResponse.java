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
public class InventoryOrderResponse {
    private UUID orderId;
    private String supplierName;
    private LocalDate orderDate;
    private double totalAmount;
    private String status;
    private LocalDate expectedDeliveryDate;
    private OrderItemResponse orderItems;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}

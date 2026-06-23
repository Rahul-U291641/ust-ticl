package com.ticl.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryOrderResponse {
    private String supplierName;
    private LocalDate orderDate;
    private double totalAmount;
    private String status;
    private LocalDate expectedDeliveryDate;
    private List<OrderItemResponse> orderItems;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}

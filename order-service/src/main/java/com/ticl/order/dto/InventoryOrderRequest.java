package com.ticl.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryOrderRequest {
    private String supplierName;
    private LocalDate orderDate;
    private double totalAmount;
    private String status;
    private LocalDate expectedDeliveryDate;
    private List<OrderItemRequest> orderItems;
}

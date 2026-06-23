package com.ticl.order.entity;

import com.ticl.commons.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "inventory_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InventoryOrders extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;
    @Column(name = "supplier_name")
    private String supplierName;
    @Column(name = "order_date")
    private LocalDate orderDate;
    @Column(name = "total_amount")
    private double totalAmount;
    @Column(name = "status")
    private String status;
    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;
}

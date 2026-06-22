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
    private UUID order_id;
    private String supplier_name;
    private LocalDate order_date;
    private double total_amount;
    private String status;
    private LocalDate expected_delivery_date;
}

package com.ticl.order.entity;

import com.ticl.commons.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OrderItems extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_item_id")
    private UUID orderItemId;
    @Column(name = "order_id")
    private UUID orderId;
    @Column(name = "item_id")
    private UUID itemId;
    @Column(name = "quantity" )
    private int quantity;
    @Column(name = "unit_price")
    private double unitPrice;
}

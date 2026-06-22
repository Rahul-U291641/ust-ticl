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
public class OrderItems  extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID order_item_id;
    private UUID order_id;
    private UUID item_id;
    private int quantity;
    private double unit_price;
}

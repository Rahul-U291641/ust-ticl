package com.ticl.inventory.entity;

import com.ticl.commons.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "inventory_items",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"item_name", "supplier"}, name = "uk_inventory_composite")
})
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "item_id")
    private UUID itemId;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "supplier")
    private String supplier;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @Column(name = "reorder_threshold")
    private int reorderThreshold;
}

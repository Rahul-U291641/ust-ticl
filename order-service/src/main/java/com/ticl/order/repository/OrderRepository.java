package com.ticl.order.repository;

import com.ticl.order.entity.InventoryOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<InventoryOrders, UUID> {
}

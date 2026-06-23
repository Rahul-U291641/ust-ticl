package com.ticl.order.controller;

import com.ticl.order.dto.InventoryOrderRequest;
import com.ticl.order.dto.InventoryOrderResponse;
import com.ticl.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<InventoryOrderResponse> createOrder(@RequestBody InventoryOrderRequest request) {
        InventoryOrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<InventoryOrderResponse>> getAllOrders() {
        List<InventoryOrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryOrderResponse> getOrderById(@PathVariable UUID id) {
        InventoryOrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<InventoryOrderResponse> updateOrderStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        InventoryOrderResponse response = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<InventoryOrderResponse>> getOrderHistory() {
        List<InventoryOrderResponse> history = orderService.getOrderHistory();
        return ResponseEntity.ok(history);
    }
}

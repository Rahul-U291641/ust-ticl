package com.ticl.order.service;

import com.ticl.commons.exception.customExceptions.BusinessException;
import com.ticl.commons.exception.customExceptions.*;
import com.ticl.order.dto.InventoryOrderRequest;
import com.ticl.order.dto.InventoryOrderResponse;
import com.ticl.order.dto.OrderItemResponse;
import com.ticl.order.entity.InventoryOrders;
import com.ticl.order.entity.OrderItems;
import com.ticl.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;

    public InventoryOrderResponse createOrder(InventoryOrderRequest request) {
        try {
            if (request == null) {
                throw new BusinessException("Order request cannot be null");
            }
            if (request.getSupplierName() == null || request.getSupplierName().trim().isEmpty()) {
                throw new BusinessException("Supplier name is required");
            }
            if (request.getOrderDate() == null) {
                throw new BusinessException("Order date is required");
            }
            if (request.getTotalAmount() <= 0) {
                throw new BusinessException("Total amount must be greater than zero");
            }
            if (request.getExpectedDeliveryDate() == null) {
                throw new BusinessException("Expected delivery date is required");
            }

            InventoryOrders order = InventoryOrders.builder()
                    .supplierName(request.getSupplierName())
                    .orderDate(request.getOrderDate())
                    .totalAmount(request.getTotalAmount())
                    .status(request.getStatus() != null ? request.getStatus() : "CREATED")
                    .expectedDeliveryDate(request.getExpectedDeliveryDate())
                    .createdAt(LocalDateTime.now())
                    .build();

            if (request.getOrderItems() != null && !request.getOrderItems().isEmpty()) {
                List<OrderItems> items = new ArrayList<>();
                for (var itemRequest : request.getOrderItems()) {
                    if (itemRequest == null) {
                        throw new BusinessException("Order item cannot be null");
                    }
                    if (itemRequest.getItemId() == null) {
                        throw new BusinessException("Item ID is required for order items");
                    }
                    if (itemRequest.getQuantity() <= 0) {
                        throw new BusinessException("Quantity must be greater than zero");
                    }
                    if (itemRequest.getUnitPrice() <= 0) {
                        throw new BusinessException("Unit price must be greater than zero");
                    }

                    OrderItems orderItem = OrderItems.builder()
                            .itemId(itemRequest.getItemId())
                            .quantity(itemRequest.getQuantity())
                            .unitPrice(itemRequest.getUnitPrice())
                            .order(order)
                            .createdAt(LocalDateTime.now())
                            .build();
                    items.add(orderItem);
                }
                order.setOrderItems(items);
            }

            InventoryOrders savedOrder = orderRepository.save(order);
            log.info("Order created successfully with id: {}", savedOrder.getOrderId());
            return mapToResponse(savedOrder);
        } catch (BusinessException ex) {
            log.error("Business error while creating order: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error while creating order", ex);
            throw new BusinessException("Failed to create order: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<InventoryOrderResponse> getAllOrders() {
        try {
            log.info("Fetching all orders");
            return orderRepository.findAll()
                    .stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error while fetching all orders", ex);
            throw new BusinessException("Failed to retrieve orders: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public InventoryOrderResponse getOrderById(UUID id) {
        try {
            if (id == null) {
                throw new BusinessException("Order id cannot be null");
            }
            log.info("Fetching order with id: {}", id);
            InventoryOrders order = orderRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
            return mapToResponse(order);
        } catch (ResourceNotFoundException ex) {
            log.warn("Order not found with id: {}", id);
            throw ex;
        } catch (Exception ex) {
            log.error("Error while fetching order with id: {}", id, ex);
            throw new BusinessException("Failed to retrieve order: " + ex.getMessage());
        }
    }

    public InventoryOrderResponse updateOrderStatus(UUID id, String status) {
        try {
            if (id == null) {
                throw new BusinessException("Order id cannot be null");
            }
            if (status == null || status.trim().isEmpty()) {
                throw new BusinessException("Status cannot be empty");
            }
            log.info("Updating order status for id: {} to: {}", id, status);
            InventoryOrders order = orderRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
            order.setStatus(status);
            InventoryOrders updatedOrder = orderRepository.save(order);
            log.info("Order status updated successfully for id: {}", id);
            return mapToResponse(updatedOrder);
        } catch (ResourceNotFoundException ex) {
            log.warn("Order not found with id: {}", id);
            throw ex;
        } catch (BusinessException ex) {
            log.error("Business error while updating order status: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error while updating order status for id: {}", id, ex);
            throw new BusinessException("Failed to update order status: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<InventoryOrderResponse> getOrderHistory() {
        try {
            log.info("Fetching order history");
            return orderRepository.findAll()
                    .stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error while fetching order history", ex);
            throw new BusinessException("Failed to retrieve order history: " + ex.getMessage());
        }
    }

    private InventoryOrderResponse mapToResponse(InventoryOrders order) {
        try {
            if (order == null) {
                throw new BusinessException("Order entity cannot be null");
            }
            if (order.getOrderId() == null) {
                throw new BusinessException("Order ID cannot be null");
            }
            if (order.getSupplierName() == null) {
                throw new BusinessException("Supplier name cannot be null");
            }
            if (order.getOrderDate() == null) {
                throw new BusinessException("Order date cannot be null");
            }
            if (order.getStatus() == null) {
                throw new BusinessException("Order status cannot be null");
            }

            List<OrderItemResponse> items = order.getOrderItems() != null 
                    ? order.getOrderItems().stream()
                    .map(this::mapOrderItemToResponse)
                    .collect(Collectors.toList())
                    : null;

            return InventoryOrderResponse.builder()
                    .orderId(order.getOrderId())
                    .supplierName(order.getSupplierName())
                    .orderDate(order.getOrderDate())
                    .totalAmount(order.getTotalAmount())
                    .status(order.getStatus())
                    .expectedDeliveryDate(order.getExpectedDeliveryDate())
                    .orderItems(items)
                    .createdAt(order.getCreatedAt() != null ? LocalDate.from(order.getCreatedAt()) : null)
                    .updatedAt(order.getUpdatedAt() != null ? LocalDate.from(order.getUpdatedAt()) : null)
                    .build();
        } catch (Exception ex) {
            log.error("Error while mapping order to response", ex);
            throw new BusinessException("Failed to map order response: " + ex.getMessage());
        }
    }

    private OrderItemResponse mapOrderItemToResponse(OrderItems item) {
        if (item == null) {
            throw new BusinessException("Order item cannot be null");
        }
        if (item.getOrderItemId() == null) {
            throw new BusinessException("Order item ID cannot be null");
        }
        if (item.getItemId() == null) {
            throw new BusinessException("Item ID cannot be null");
        }

        return OrderItemResponse.builder()
                .orderItemId(item.getOrderItemId())
                .itemId(item.getItemId())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .createdAt(item.getCreatedAt() != null ? LocalDate.from(item.getCreatedAt()) : null)
                .updatedAt(item.getUpdatedAt() != null ? LocalDate.from(item.getUpdatedAt()) : null)
                .build();
    }
}

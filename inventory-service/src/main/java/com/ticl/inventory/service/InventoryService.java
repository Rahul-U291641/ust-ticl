package com.ticl.inventory.service;

import com.ticl.commons.exception.customExceptions.BusinessException;
import com.ticl.inventory.dto.EventType;
import com.ticl.inventory.dto.InventoryRequest;
import com.ticl.inventory.dto.InventoryResponse;
import com.ticl.inventory.entity.InventoryItem;
import com.ticl.inventory.producer.InventoryEventProducer;
import com.ticl.inventory.repository.InventoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository repository;
    private final InventoryEventProducer producer;

    public InventoryResponse create(InventoryRequest request) {
        try {
            InventoryItem item = InventoryItem.builder()
                    .itemName(request.getItemName())
                    .supplier(request.getSupplier())
                    .quantity(request.getQuantity())
                    .unitPrice(request.getUnitPrice())
                    .reorderThreshold(request.getReorderThreshold())
                    .createdAt(LocalDateTime.now())
                    .build();

            repository.save(item);
            producer.publishInventoryEvent(item, EventType.INVENTORY_CREATED);

            return mapToResponse(item);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Item already present with the combination of item_id, item_name, and supplier.");
        } catch (Exception e) {
            throw new BusinessException("Unable to create a Inventory item due to : " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Page<InventoryResponse> getAll(Pageable pageable) {
        Page<InventoryItem> items = repository.findAll(pageable);
        return items.map(this::mapToResponse);
    }

    private InventoryResponse mapToResponse(InventoryItem item) {
        return InventoryResponse.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .supplier(item.getSupplier())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .reorderThreshold(item.getReorderThreshold())
                .build();
    }

    public InventoryResponse getById(UUID id) {
        try {
            InventoryItem inventoryItem = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Inventory item not found for requested ID : " + id));
            return mapToResponse(inventoryItem);
        } catch (ResourceNotFoundException e) {
            throw new BusinessException("Failed to get inventory item for : " + id + " with cause : " + e.getMessage());
        }
    }

    public void update(UUID id, @Valid InventoryRequest request) {
        try {
            InventoryItem inventoryItem = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("The item not found for requested ID : " + id));

            inventoryItem.setItemName(request.getItemName());
            inventoryItem.setItemId(id);
            inventoryItem.setQuantity(request.getQuantity());
            inventoryItem.setSupplier(request.getSupplier());
            inventoryItem.setUnitPrice(request.getUnitPrice());
            inventoryItem.setReorderThreshold(request.getReorderThreshold());
            inventoryItem.setUpdatedAt(LocalDateTime.now());

            repository.save(inventoryItem);
            //FIXME:: producer.publishInventoryEvent(inventoryItem, EventType.INVENTORY_UPDATED);
        } catch (Exception ex) {
            throw new BusinessException("Failed to update inventory item for : " + id + " with cause : " + ex.getMessage());
        }
    }

    public void delete(UUID id) {
        try {
            InventoryItem inventoryItem = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("The item not found for requested ID : " + id));
            repository.deleteById(id);
            //FIXME:: producer.publishInventoryEvent(inventoryItem, EventType.INVENTORY_UPDATED);
        } catch (Exception ex) {
            throw new BusinessException("Failed to delete inventory item for : " + id + " with cause : " + ex.getMessage());
        }
    }
}

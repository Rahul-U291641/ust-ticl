package com.ticl.inventory.controller;

import com.ticl.inventory.dto.InventoryRequest;
import com.ticl.inventory.dto.InventoryResponse;
import com.ticl.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @PostMapping
    @PreAuthorize("hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<?> create(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public ResponseEntity<Page<InventoryResponse>> getAll(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "itemName")
            Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public InventoryResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('INVENTORY_MANAGER')")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @Valid @RequestBody
            InventoryRequest request) {

        service.update(id, request);

        return ResponseEntity.ok("Inventory item updated successfully!");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Inventory item deleted successfully!");
    }
}
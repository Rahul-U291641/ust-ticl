package com.ticl.commons.enums;

import lombok.Getter;

@Getter
public enum EventType {
    INVENTORY_CREATED("Inventory item has been created successfully."),
    INVENTORY_UPDATED("Inventory item has been updated successfully."),
    INVENTORY_DELETED("Inventory item has been deleted successfully."),
    INVENTORY_LOW_STOCK("Inventory quantity has fallen below the reorder threshold."),
    INVENTORY_REORDER("Inventory reorder has been initiated due to low stock."),

    ORDER_CREATED("Order has been created successfully."),
    ORDER_UPDATED("Order has been updated successfully."),
    ORDER_CONFIRMED("Order has been confirmed successfully."),
    ORDER_CANCELLED("Order has been cancelled successfully."),
    ORDER_SHIPPED("Order has been shipped successfully."),
    ORDER_DELIVERED("Order has been delivered successfully."),

    USER_LOGGED_IN("User logged in successfully."),
    USER_LOGGED_OUT("User logged out successfully.");

    private final String description;

    EventType(String description) {
        this.description = description;
    }

}

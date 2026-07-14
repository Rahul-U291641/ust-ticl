package com.ticl.commons.constant;

public final class KafkaTopics {
    // Audit
    public static final String AUDIT_EVENTS = "audit-events";

    // Inventory
    public static final String INVENTORY_CREATED = "inventory-created";
    public static final String INVENTORY_UPDATED = "inventory-updated";
    public static final String INVENTORY_DELETED = "inventory-deleted";
    public static final String INVENTORY_LOW_STOCK = "inventory-low-stock";
    public static final String INVENTORY_REORDER = "inventory-reorder";

    // Orders
    public static final String ORDER_CREATED = "order-created";
    public static final String ORDER_UPDATED = "order-updated";
    public static final String ORDER_CONFIRMED = "order-confirmed";
    public static final String ORDER_CANCELLED = "order-cancelled";
    public static final String ORDER_SHIPPED = "order-shipped";
    public static final String ORDER_DELIVERED = "order-delivered";

    // Authentication
    public static final String USER_LOGIN = "user-login";
    public static final String USER_LOGOUT = "user-logout";

    // Dead Letter Topics
    public static final String ORDER_DLT = "order-dlt";
    public static final String INVENTORY_DLT = "inventory-dlt";
    public static final String AUDIT_DLT = "audit-dlt";
}

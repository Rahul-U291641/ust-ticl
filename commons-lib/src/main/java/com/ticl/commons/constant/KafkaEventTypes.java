package com.ticl.commons.constant;

public final class KafkaEventTypes {
    private KafkaEventTypes() {
    }

    public static final String INVENTORY_CREATED = "INVENTORY_CREATED";
    public static final String INVENTORY_UPDATED = "INVENTORY_UPDATED";
    public static final String INVENTORY_DELETED = "INVENTORY_DELETED";
    public static final String INVENTORY_LOW_STOCK = "INVENTORY_LOW_STOCK";

    public static final String ORDER_CREATED = "ORDER_CREATED";
    public static final String ORDER_UPDATED = "ORDER_UPDATED";
    public static final String ORDER_CONFIRMED = "ORDER_CONFIRMED";
    public static final String ORDER_CANCELLED = "ORDER_CANCELLED";
    public static final String ORDER_DELIVERED = "ORDER_DELIVERED";

    public static final String USER_LOGIN = "USER_LOGIN";
    public static final String USER_LOGOUT = "USER_LOGOUT";
}

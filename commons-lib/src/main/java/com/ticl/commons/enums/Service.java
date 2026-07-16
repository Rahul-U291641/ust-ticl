package com.ticl.commons.enums;

import lombok.Getter;

@Getter
public enum Service {
    AUTH("AUTH_SERVICE"),
    INVENTORY("INVENTORY_SERVICE"),
    ORDER("ORDER_SERVICE"),
    AUDIT("AUDIT_SERVICE"),
    ALERT("ALERT_SERVICE");

    private final String service;

    Service(String service) {
        this.service = service;
    }
}

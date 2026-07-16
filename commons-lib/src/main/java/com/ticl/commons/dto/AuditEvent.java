package com.ticl.commons.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditEvent {
    private UUID eventId;
    private String serviceName;
    private String entityType;
    private String entityId;
    private String eventType;
    private String performedBy;
    private LocalDateTime eventTime;
    private String description;
    private Map<String, Object> metadata;
}

package com.ticl.audit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID auditId;
    private UUID eventId;
    private String serviceName;
    private String entityType;
    private String entityId;
    private String eventType;
    private String performedBy;
    private LocalDateTime eventTime;
    @Column(length = 1000)
    private String description;
    @Lob
    private String metadata;
    private LocalDateTime createdAt;
}

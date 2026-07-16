package com.ticl.inventory.producer;

import com.ticl.commons.constant.KafkaTopics;
import com.ticl.commons.dto.AuditEvent;
import com.ticl.commons.enums.EventType;
import com.ticl.commons.utils.SecurityUtils;
import com.ticl.inventory.entity.InventoryItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class InventoryEventProducer {

    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;

    public void publishInventoryEvent(InventoryItem item, EventType eventType) {

        log.info("Publishing audit event for inventory item: {} with event type: {}", item.getItemId(), eventType);

        AuditEvent auditEvent = AuditEvent.builder()
                .eventId(UUID.randomUUID())
                .serviceName(com.ticl.commons.enums.Service.INVENTORY.getService())
                .entityType(com.ticl.commons.enums.Service.INVENTORY.name())
                .entityId(item.getItemId().toString())
                .eventType(eventType.name())
                .performedBy(SecurityUtils.getCurrentUsername() +" : "+SecurityUtils.getCurrentRole())
                .eventTime(LocalDateTime.now())
                .description(eventType.getDescription())
                .metadata(Map.of(
                        "itemId", item.getItemId(),
                        "itemName", item.getItemName(),
                        "quantity", item.getQuantity(),
                        "supplier", item.getSupplier(),
                        "recordThreshold", item.getReorderThreshold()))
                .build();

        kafkaTemplate.send(KafkaTopics.AUDIT_EVENTS, item.getItemId().toString(), auditEvent);
        log.info("Audit event published successfully for inventory item: {}", item.getItemId());
    }
}

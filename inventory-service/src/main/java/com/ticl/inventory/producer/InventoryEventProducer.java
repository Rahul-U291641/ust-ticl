package com.ticl.inventory.producer;

import com.ticl.inventory.dto.EventType;
import com.ticl.inventory.dto.InventoryEvent;
import com.ticl.inventory.entity.InventoryItem;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryEventProducer {

    private final KafkaTemplate<String, InventoryEvent> kafkaTemplate;

    public void publishInventoryEvent(InventoryItem item, EventType eventType) {

        InventoryEvent event =
                InventoryEvent.builder()
                        .itemId(item.getItem_id())
                        .itemName(item.getItem_name())
                        .quantity(item.getQuantity())
                        .reorderThreshold(item.getReorder_threshold())
                        .eventType(eventType.name())
                        .eventTime(LocalDateTime.now())
                        .build();

        kafkaTemplate.send(
                "inventory-events",
                item.getItem_id().toString(),
                event);
    }
}

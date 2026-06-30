package com.ticl.inventory.producer;

import com.ticl.inventory.dto.EventType;
import com.ticl.inventory.dto.InventoryEvent;
import com.ticl.inventory.entity.InventoryItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryEventProducer {

    @Value("${app.kafka.inventory-topic}")
    private String INVENTORY_TOPIC;

    private final KafkaTemplate<String, InventoryEvent> kafkaTemplate;

    public void publishInventoryEvent(InventoryItem item, EventType eventType) {

        InventoryEvent event =
                InventoryEvent.builder()
                        .itemId(item.getItemId())
                        .itemName(item.getItemName())
                        .quantity(item.getQuantity())
                        .reorderThreshold(item.getReorderThreshold())
                        .eventType(eventType.name())
                        .eventTime(LocalDateTime.now())
                        .build();

        kafkaTemplate.send(INVENTORY_TOPIC, item.getItemId().toString(), event);
    }
}

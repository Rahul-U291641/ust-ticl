package com.ticl.audit.consumer;

import com.ticl.audit.service.AuditService;
import com.ticl.commons.constant.KafkaConsumerGroups;
import com.ticl.commons.constant.KafkaTopics;
import com.ticl.commons.dto.AuditEvent;
import com.ticl.commons.exception.customExceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditEventConsumer {
    private final AuditService auditService;

    @KafkaListener(
            topics = KafkaTopics.AUDIT_EVENTS,
            groupId = KafkaConsumerGroups.AUDIT_GROUP)
    public void consumeInventoryEvent(AuditEvent event, Acknowledgment ack) {
        log.info("Received Audit Event : {}", event.getEventType());
        try {
            auditService.save(event);
            ack.acknowledge();
        } catch (Exception e) {
            throw new BusinessException("Unable to save the audit entry for event ID " + event.getEventId() + " due to : " + e.getMessage());
        }
    }

    @KafkaListener(
            topics = {
                    KafkaTopics.USER_LOGIN,
                    KafkaTopics.USER_LOGOUT},
            groupId = KafkaConsumerGroups.AUDIT_GROUP
    )
    public void consumeUserEvent(AuditEvent event, Acknowledgment ack) {
        log.info("Received Audit Event : {}", event.getEventType());
        try {
            auditService.save(event);
            ack.acknowledge();
        } catch (Exception e) {
            throw new BusinessException("Unable to save the audit entry for event ID " + event.getEventId() + " due to : " + e.getMessage());
        }
    }
}

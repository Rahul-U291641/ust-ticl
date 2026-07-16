package com.ticl.auth.producer;

import com.ticl.auth.entity.User;
import com.ticl.commons.constant.KafkaTopics;
import com.ticl.commons.dto.AuditEvent;
import com.ticl.commons.enums.EventType;
import com.ticl.commons.utils.SecurityUtils;
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
public class AuthEventProducer {
    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;

    public void publishUserAuthEvent(User user, EventType eventType) {

        log.info("Publishing audit event for user: {} with event type: {}", user.getUser_id(), eventType);

        AuditEvent auditEvent = AuditEvent.builder()
                .eventId(UUID.randomUUID())
                .serviceName(com.ticl.commons.enums.Service.AUTH.getService())
                .entityType(com.ticl.commons.enums.Service.AUTH.name())
                .entityId(user.getUser_id().toString())
                .eventType(eventType.name())
                .performedBy(SecurityUtils.getCurrentUsername() + " : " + SecurityUtils.getCurrentRole())
                .eventTime(LocalDateTime.now())
                .description(eventType.getDescription())
                .metadata(Map.of(
                        "userId", user.getUser_id(),
                        "username", user.getUsername(),
                        "role", user.getRole()))
                .build();

        sendEvent(auditEvent);
        log.info("Audit event published successfully for User : {}", user.getUser_id());
    }

    private void sendEvent(AuditEvent auditEvent) {
        String topic = EventType.USER_LOGGED_IN.name().equals(auditEvent.getEventType())?
                KafkaTopics.USER_LOGIN :
                KafkaTopics.USER_LOGOUT;
        log.info("Sending user login event to kafka topic: {}", KafkaTopics.USER_LOGIN);
        kafkaTemplate.send(topic, auditEvent.getEventId().toString(), auditEvent);
    }
}

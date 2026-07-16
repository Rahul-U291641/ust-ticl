package com.ticl.audit.service;

import com.ticl.audit.entity.AuditHistory;
import com.ticl.audit.repository.AuditRepository;
import com.ticl.commons.dto.AuditEvent;
import com.ticl.commons.exception.customExceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {
    private final AuditRepository repository;
    private final ObjectMapper objectMapper;

    public void save(AuditEvent event) {
        try {
            AuditHistory history = AuditHistory.builder()
                    .eventId(event.getEventId())
                    .serviceName(event.getServiceName())
                    .entityType(event.getEntityType())
                    .entityId(event.getEntityId())
                    .eventType(event.getEventType())
                    .performedBy(event.getPerformedBy())
                    .eventTime(event.getEventTime())
                    .description(event.getDescription())
                    .metadata(objectMapper.writeValueAsString(event.getMetadata()))
                    .createdAt(LocalDateTime.now())
                    .build();

            repository.save(history);
            log.info("Audit history stored successfully.");
        } catch (Exception ex) {
            log.error("Unable to store audit history", ex);
            throw new BusinessException("Unable to store audit history due to : " + ex.getMessage());
        }
    }
}

package com.example.academic_system.services;

import com.example.academic_system.models.ActivityLog;
import com.example.academic_system.repositories.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    public void log(String entityName, String entityId, String action, String detail, String performedBy) {
        if (entityId == null || entityId.trim().isEmpty()) {
            entityId = UUID.randomUUID().toString(); // auto-generate ID
        }

        ActivityLog log = new ActivityLog();
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setDetail(detail);
        log.setPerformedBy(performedBy);
        log.setTimestamp(new Date());

        activityLogRepository.save(log);
    }
}

package com.example.academic_system.repositories;

import com.example.academic_system.models.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findTop10ByOrderByTimestampDesc();
}

package com.example.academic_system.repositories;

import com.example.academic_system.models.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findTop10ByOrderByTimestampDesc();

    // Search by action type
    List<ActivityLog> findByActionContainingIgnoreCaseOrderByTimestampDesc(String action);

    // Search by entity name
    List<ActivityLog> findByEntityNameContainingIgnoreCaseOrderByTimestampDesc(String entityName);

    // Search by performed by (user)
    List<ActivityLog> findByPerformedByContainingIgnoreCaseOrderByTimestampDesc(String performedBy);

    // Search by detail
    List<ActivityLog> findByDetailContainingIgnoreCaseOrderByTimestampDesc(String detail);

    // Combined search
    @Query("SELECT a FROM ActivityLog a WHERE " +
            "LOWER(a.action) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.entityName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.performedBy) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.detail) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY a.timestamp DESC")
    List<ActivityLog> searchLogs(@Param("keyword") String keyword);

    // Filter by specific action types
    List<ActivityLog> findByActionInOrderByTimestampDesc(List<String> actions);

    List<ActivityLog> findAllByOrderByTimestampDesc();
}
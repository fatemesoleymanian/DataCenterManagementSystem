package com.example.DataCenterManagementSystem.repository;

import com.example.DataCenterManagementSystem.entity.security.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
}

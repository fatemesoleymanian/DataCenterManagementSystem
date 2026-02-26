package com.example.DataCenterManagementSystem.service;

import com.example.DataCenterManagementSystem.dto.ActivityLogResponse;
import com.example.DataCenterManagementSystem.entity.security.ActivityLog;
import com.example.DataCenterManagementSystem.exception.NotFoundException;
import com.example.DataCenterManagementSystem.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ActivityLogResponse getActivityLogById(Long id) {
        return activityLogRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("ActivityLog not found with id: " + id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public List<ActivityLogResponse> getAllActivityLogs() {
        return activityLogRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Mapper method
    private ActivityLogResponse mapToResponse(ActivityLog entity) {
        return new ActivityLogResponse(
                entity.getId(),
                entity.getAction(),
                entity.getUsername(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getLastModifiedBy()
        );
    }
}

package com.example.DataCenterManagementSystem.dto;

import java.time.LocalDateTime;

public record ActivityLogResponse(
        Long id,
        String action,
        String username,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String lastModifiedBy
) {
}

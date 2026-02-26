package com.example.DataCenterManagementSystem.dto.crm;

import java.time.LocalDateTime;

public record CustomerServiceResponse(
        Long id,
        Long customerId,
        Long serverId,
        LocalDateTime expiryDate,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String lastModifiedBy
) {}

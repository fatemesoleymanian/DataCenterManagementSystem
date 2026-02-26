package com.example.DataCenterManagementSystem.dto.dcim;

import java.time.LocalDateTime;

public record RackUnitResponse(
        Long id,
        int unitNumber,
        Long occupiedById,
        Long rackId,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String lastModifiedBy
) {}
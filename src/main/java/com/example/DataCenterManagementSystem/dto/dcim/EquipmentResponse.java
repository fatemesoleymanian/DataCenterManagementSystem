package com.example.DataCenterManagementSystem.dto.dcim;

import com.example.DataCenterManagementSystem.entity.dcim.EquipmentType;

import java.time.LocalDateTime;
import java.util.List;

public record EquipmentResponse(
        Long id,
        String modelName,
        EquipmentType type,
        int unitSize,
        List<Long> occupiedUnitIds,
        Long rackId,
        List<Long> portIds,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String lastModifiedBy
) {
}

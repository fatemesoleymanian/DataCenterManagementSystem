package com.example.DataCenterManagementSystem.dto.dcim;

import com.example.DataCenterManagementSystem.entity.dcim.EquipmentType;
import jakarta.validation.constraints.*;

public record CreateEquipmentRequest(

        @NotBlank
        String modelName,

        @NotNull
        EquipmentType type,

        @Positive
        int unitSize,

        @NotNull
        Long rackId,

        @Min(1)
        int startUnit,

        @PositiveOrZero
        int portCount
) {}
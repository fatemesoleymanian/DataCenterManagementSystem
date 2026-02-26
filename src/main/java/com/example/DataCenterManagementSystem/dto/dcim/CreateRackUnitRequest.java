package com.example.DataCenterManagementSystem.dto.dcim;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public record CreateRackUnitRequest(
        @NotNull(message = "Unit number is required")
        @Min(value = 1, message = "Unit number must be at least 1")
        int unitNumber,

        @NotNull(message = "Rack ID is required")
        Long rackId
) {}



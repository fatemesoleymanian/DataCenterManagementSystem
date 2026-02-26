package com.example.DataCenterManagementSystem.dto.dcim;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


public record CreateLocationRequest(

        @NotBlank(message = "Name is required")
        String name,

        @Min(value = 1)
        @Max(value = 4)
        Integer level,

        Long parentId
) {}
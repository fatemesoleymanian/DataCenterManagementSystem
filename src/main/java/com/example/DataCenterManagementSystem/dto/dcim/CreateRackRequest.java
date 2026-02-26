package com.example.DataCenterManagementSystem.dto.dcim;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateRackRequest {

    @NotNull(message = "Rack number is required")
    private Integer rackNumber;

    @NotNull(message = "RackRow ID is required")
    private Long rackRowId;
}